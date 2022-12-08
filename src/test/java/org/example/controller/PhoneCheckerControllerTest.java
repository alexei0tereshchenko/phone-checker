package org.example.controller;

import org.example.dto.CommonDto;
import org.example.service.PhoneCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PhoneCheckerControllerTest extends AbstractControllerTest {
    @Mock
    private PhoneCheckerService phoneCheckerService;

    private final String baseUrl = "/phone-check";

    @InjectMocks
    private PhoneCheckerController controller;

    @BeforeEach
    protected void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("urls.phone-check", baseUrl).build();
    }

    @Test
    void checkCountry() throws Exception {
        String expected = "China";
        when(phoneCheckerService.getCountriesByPhoneNumber(anyString()))
                .thenReturn(new CommonDto<>(expected));


        headers.add("phone", "+123 (32) 12-12-12");
        mvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.body").value(expected));

        verify(phoneCheckerService, times(1)).getCountriesByPhoneNumber(anyString());
    }

    @Test
    void updatePrefixDirectory() throws Exception {
        doNothing().when(phoneCheckerService).updateDbFromWiki();

        mvc.perform(post(baseUrl + "/update")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(phoneCheckerService, times(1)).updateDbFromWiki();
    }


}
