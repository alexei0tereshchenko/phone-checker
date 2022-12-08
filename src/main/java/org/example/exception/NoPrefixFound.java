package org.example.exception;

import lombok.Getter;

@Getter
public class NoPrefixFound extends RuntimeException {

    public NoPrefixFound() {
        super("No countries were found with prefix of the provided phone number");
    }
}
