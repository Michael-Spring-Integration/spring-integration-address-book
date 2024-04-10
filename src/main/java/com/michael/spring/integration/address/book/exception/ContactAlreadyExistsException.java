package com.michael.spring.integration.address.book.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContactAlreadyExistsException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;
    private final String message;
}
