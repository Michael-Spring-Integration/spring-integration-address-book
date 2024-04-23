package com.michael.spring.integration.address.book.handler;

import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class GlobalErrorHandlerTest {

    @Test
    public void testHandleErrorForInvalidContactDetailsException(){
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();

        String objectName = "contactDTO.emailAddress.invalid";
        String defaultMessage = "Invalid email address";
        List<ObjectError> objectErrors = new ArrayList<>();
        ObjectError objectError = new ObjectError(objectName,defaultMessage);
        objectErrors.add(objectError);

        InvalidContactDetailsException invalidContactDetailsException = Assertions.assertThrows(InvalidContactDetailsException.class,() -> {
            throw new InvalidContactDetailsException(objectErrors);
        });

        MessageConversionException messageConversionException = Assertions.assertThrows(MessageConversionException.class,() -> {
            throw new MessageConversionException("Invalid email address",invalidContactDetailsException);
        });
        ErrorMessage errorMessage = new ErrorMessage(messageConversionException);

        Message<Object> message = globalErrorHandler.handleError(errorMessage);
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getPayload());
        Assertions.assertEquals("Invalid email address",message.getPayload().toString());
        Assertions.assertNotNull(message.getHeaders());
        Assertions.assertEquals(4,message.getHeaders().size());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,message.getHeaders().get("http_statusCode"));
    }

}
