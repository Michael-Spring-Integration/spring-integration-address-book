package com.michael.spring.integration.address.book.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
public class ContactNotFoundException extends RuntimeException{
    private final String resourceName;
    private final String fieldName;
    private final String message;
}
