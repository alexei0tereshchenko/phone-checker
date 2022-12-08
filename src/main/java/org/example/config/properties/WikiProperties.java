package org.example.config.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "app.wiki")
@Slf4j
@Data
public class WikiProperties {
    private String articleUrl;
    private String countryPhoneLineTag;
    private String countryPhoneTag;
    private String phoneFirstChar;
    private String phoneDiffSeparator;
    private String phoneAnyNumMask;


    @PostConstruct
    public void init() {
        log.info("{}", this);
    }
}

