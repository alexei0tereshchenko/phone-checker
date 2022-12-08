package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommonDto;
import org.example.exception.WikiParseException;
import org.example.service.PhoneCheckerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${urls.phone-check}")
@RequiredArgsConstructor
public class PhoneCheckerController {

    private final PhoneCheckerService phoneCheckerService;

    @GetMapping
    public CommonDto<String> checkCountry(@RequestHeader("phone") String phoneNumber) {
        return phoneCheckerService.getCountriesByPhoneNumber(phoneNumber);
    }

    @PostMapping("/update")
    public void updatePrefixDirectory() {
        try {
            phoneCheckerService.updateDbFromWiki();
        } catch (IOException e) {
            throw new WikiParseException(e);
        }
    }
}
