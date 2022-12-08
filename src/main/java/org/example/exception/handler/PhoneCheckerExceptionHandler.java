package org.example.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidInputPhoneFormat;
import org.example.exception.NoPrefixFound;
import org.example.exception.advise.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class PhoneCheckerExceptionHandler {

    @ExceptionHandler(InvalidInputPhoneFormat.class)
    public ResponseEntity<Response> handleInvalidInputPhoneFormatException(InvalidInputPhoneFormat e) {
        log.error(e.getMessage());
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPrefixFound.class)
    public ResponseEntity<Response> handleInvalidInputPhoneFormatException(NoPrefixFound e) {
        log.error(e.getMessage());
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
