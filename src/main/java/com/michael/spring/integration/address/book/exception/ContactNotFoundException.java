package com.michael.spring.integration.address.book.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@AllArgsConstructor
@Getter
public class ContactNotFoundException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;
    private final long fieldValue;
    private final String message;
}
