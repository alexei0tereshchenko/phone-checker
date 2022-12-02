package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommonDto;
import org.example.service.PhoneCheckerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${urls.phone-check}")
@RequiredArgsConstructor
public class PhoneCheckerController {

    private final PhoneCheckerService phoneCheckerService;

    @GetMapping
    public CommonDto<String> checkCountry(@RequestHeader("phone") String phoneNumber) {
        return phoneCheckerService.getCountriesByPhoneNumber(phoneNumber);
    }
}
