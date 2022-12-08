package org.example.exception;

public class WikiParseException extends RuntimeException {
    public WikiParseException(Exception e) {
        super("Couldn't parse wiki article. Please check the provided link", e);
    }
}
