package com.michael.spring.integration.address.book.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

 class InvalidContactDetailsExceptionTest {

    @Test
     void testInvalidContactDetailsException(){

        String objectName = "contactDTO.emailAddress.invalid";
        String defaultMessage = "Invalid email address";
        List<ObjectError> objectErrors = new ArrayList<>();
        ObjectError objectError = new ObjectError(objectName,defaultMessage);
        objectErrors.add(objectError);

        Throwable cause = null;
        InvalidContactDetailsException invalidContactDetailsException = Assertions.assertThrows(InvalidContactDetailsException.class,() -> {
            throw new InvalidContactDetailsException(objectErrors);
        });
        Assertions.assertNotNull(invalidContactDetailsException);
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors());
        Assertions.assertEquals(1,invalidContactDetailsException.getObjectErrors().size());
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(0));
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(0).getObjectName());
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(0).getDefaultMessage());

    }
}
