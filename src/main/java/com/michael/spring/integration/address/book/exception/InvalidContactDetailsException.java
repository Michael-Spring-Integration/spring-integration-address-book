package com.michael.spring.integration.address.book.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@AllArgsConstructor
@Getter
public class InvalidContactDetailsException extends RuntimeException {
    private final List<ObjectError> objectErrors;
}
