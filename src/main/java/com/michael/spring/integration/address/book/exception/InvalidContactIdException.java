package com.michael.spring.integration.address.book.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidContactIdException extends RuntimeException{
    private final String fieldName;
    private final String message;
}
