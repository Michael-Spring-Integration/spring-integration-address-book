package com.michael.spring.integration.address.book.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class InvalidContactDetailsException extends RuntimeException {

    private final String resourceName;
    private final List<String> fieldNames;
    private final List<String> messages;
}
