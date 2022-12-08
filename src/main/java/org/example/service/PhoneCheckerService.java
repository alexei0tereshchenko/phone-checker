package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.properties.WikiProperties;
import org.example.dto.CommonDto;
import org.example.jpa.entity.CountryPhone;
import org.example.jpa.repository.CountryPhoneRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneCheckerService {

    private final CountryPhoneRepository countryPhoneRepository;
    private final WikiProperties wikiProperties;

    @Transactional(readOnly = true)
    public CommonDto<String> getCountriesByPhoneNumber(String phoneNumber) {
        String formattedPhone = formatPhone(phoneNumber);

        Set<String> countries = countryPhoneRepository.findCountryByPhone(formattedPhone);

        return new CommonDto<>(String.join(", ", countries));
    }

    //@PostConstruct
    @Transactional
    public void updateDbFromWiki() throws IOException {
        countryPhoneRepository.deleteAll();
        Document doc = Jsoup.connect(wikiProperties.getArticleUrl()).get();
        Elements lis = doc.select(wikiProperties.getCountryPhoneLineTag());

        Set<CountryPhone> countryPhones = new HashSet<>();

        lis.forEach(li -> {
            Elements as = li.select(wikiProperties.getCountryPhoneTag());
            if (checkIfLineIsPhoneNumber(as)) {
                String phonePrefix = as.get(0).text();
                if (phonePrefix.contains(wikiProperties.getPhoneDiffSeparator())) {
                    countryPhones.addAll(processDifferentPhonePrefix(as, phonePrefix));
                } else {
                    CountryPhone countryPhone = new CountryPhone(phonePrefix.replace(" ", "")
                            .replace(wikiProperties.getPhoneFirstChar(), ""), as.get(1).text());
                    countryPhones.add(countryPhone);
                }
            }
        });

        countryPhoneRepository.saveAll(countryPhones);
    }

    private Set<CountryPhone> processDifferentPhonePrefix(Elements as, String phonePrefix) {
        Set<CountryPhone> countryPhones = new HashSet<>();
        if (phonePrefix.contains(wikiProperties.getPhoneAnyNumMask())) {
            phonePrefix = phonePrefix.replace(wikiProperties.getPhoneAnyNumMask(), "");
        }
        String[] phonePrefixSections = phonePrefix.split(" ");
        String phonePrefixFirstSection = phonePrefixSections[0];
        phonePrefix = phonePrefix.replace(phonePrefixFirstSection, "")
                .replace(" ", "");
        String[] phonePrefixDiffs = phonePrefix.split(wikiProperties.getPhoneDiffSeparator());
        for (String phonePrefixDiff : phonePrefixDiffs) {
            phonePrefixDiff = phonePrefixFirstSection + " " + phonePrefixDiff;
            CountryPhone countryPhone = new CountryPhone(phonePrefixDiff.replace(" ", "")
                    .replace("+", ""),
                    as.get(1).text());

            countryPhones.add(countryPhone);
        }
        return countryPhones;
    }

    private boolean checkIfLineIsPhoneNumber(Elements as) {
        return as.size() >= 2 && as.get(0).text().startsWith(wikiProperties.getPhoneFirstChar());
    }

    private String formatPhone(String phoneNumber) {
        return phoneNumber.replace("+", "")
                .replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "");
    }
}
