package org.example.service;

import org.example.config.properties.WikiProperties;
import org.example.dto.CommonDto;
import org.example.exception.InvalidInputPhoneFormat;
import org.example.exception.NoPrefixFound;
import org.example.jpa.repository.CountryPhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneCheckerServiceTest {
    @Mock
    private CountryPhoneRepository repository;

    @InjectMocks
    private PhoneCheckerService phoneCheckerService;

    @BeforeEach
    protected void setUp() {
        WikiProperties wikiProperties = new WikiProperties();
        wikiProperties.setArticleUrl("https://en.wikipedia.org/wiki/List_of_country_calling_codes#Ordered_by_code");
        wikiProperties.setCountryPhoneTag("a");
        wikiProperties.setCountryPhoneLineTag("ul li");
        wikiProperties.setPhoneFirstChar("+");
        wikiProperties.setPhoneDiffSeparator("/");
        wikiProperties.setPhoneAnyNumMask("x");

        String phoneMask = "^(\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";

        ReflectionTestUtils.setField(phoneCheckerService, "wikiProperties", wikiProperties);
        ReflectionTestUtils.setField(phoneCheckerService, "phoneMask", phoneMask);

    }

    @Test
    void getCountriesByPhoneNumber_InvalidFormat() {

        InvalidInputPhoneFormat thrown = Assertions.assertThrows(InvalidInputPhoneFormat.class,
                () -> phoneCheckerService.getCountriesByPhoneNumber("+0123 (23) 12-12-xxxasdf"));

        assertEquals("Input phone number has invalid format", thrown.getMessage());
    }

    @Test
    void getCountriesByPhoneNumber_NotFound() {

        NoPrefixFound thrown = Assertions.assertThrows(NoPrefixFound.class,
                () -> phoneCheckerService.getCountriesByPhoneNumber("+0123 (23) 12-12-12"));

        assertEquals("No countries were found with prefix of the provided phone number", thrown.getMessage());
    }

    @Test
    void getCountriesByPhoneNumber() {
        when(repository.findCountryByPhone(anyString())).thenReturn(Optional.of(Arrays.asList("China", "Canada")));

        final CommonDto<String> result = phoneCheckerService.getCountriesByPhoneNumber("+123 (23) 12-12-12");

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isEmpty());
        assertEquals("China, Canada", result.getBody());

        verify(repository, times(1)).findCountryByPhone(anyString());
    }

    @Test
    void updateDbFromWiki() throws Exception {
        doNothing().when(repository).deleteAll();
        when(repository.saveAll(any())).thenReturn(null);

        phoneCheckerService.updateDbFromWiki();

        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).saveAll(any());
    }
}
