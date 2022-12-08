package org.example.exception;

import lombok.Getter;

@Getter
public class InvalidInputPhoneFormat extends RuntimeException {

    public InvalidInputPhoneFormat() {
        super("Input phone number has invalid format");
    }
}
