package com.michael.spring.integration.address.book.handler;

import com.michael.spring.integration.address.book.exception.ContactAlreadyExistsException;
import com.michael.spring.integration.address.book.exception.ContactNotFoundException;
import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import com.michael.spring.integration.address.book.exception.InvalidContactIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.HttpServerErrorException;

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

    @Test
    public void testHandleErrorForInvalidContactIdException(){
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();
        InvalidContactIdException invalidContactIdException = Assertions.assertThrows(InvalidContactIdException.class,() -> {
            throw new InvalidContactIdException("contactId","ContactId is invalid");
        });

        MessageHandlingException messageHandlingException = Assertions.assertThrows(MessageHandlingException.class,() -> {
            throw new MessageHandlingException(MessageBuilder.withPayload("Invalid contact id").build(),invalidContactIdException);
        });
        ErrorMessage errorMessage = new ErrorMessage(messageHandlingException);

        Message<Object> message = globalErrorHandler.handleError(errorMessage);
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getPayload());
        Assertions.assertEquals("ContactId is invalid",message.getPayload().toString());
        Assertions.assertNotNull(message.getHeaders());
        Assertions.assertEquals(4,message.getHeaders().size());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,message.getHeaders().get("http_statusCode"));
    }

    @Test
    public void testHandleErrorForContactNotFoundException(){
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();
        ContactNotFoundException contactNotFoundException = Assertions.assertThrows(ContactNotFoundException.class,() -> {
            throw new ContactNotFoundException("GET","contactId","The contact is not found for the given Contact ID :7");
        });

        MessageHandlingException messageHandlingException = Assertions.assertThrows(MessageHandlingException.class,() -> {
            throw new MessageHandlingException(MessageBuilder.withPayload("The contact is not found for the given Contact ID :7").build(),contactNotFoundException);
        });
        ErrorMessage errorMessage = new ErrorMessage(messageHandlingException);

        Message<Object> message = globalErrorHandler.handleError(errorMessage);
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getPayload());
        Assertions.assertEquals("The contact is not found for the given Contact ID :7",message.getPayload().toString());
        Assertions.assertNotNull(message.getHeaders());
        Assertions.assertEquals(4,message.getHeaders().size());
        Assertions.assertEquals(HttpStatus.NOT_FOUND,message.getHeaders().get("http_statusCode"));
    }

    @Test
    public void testHandleErrorForContactAlreadyExistsException(){
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();
        ContactAlreadyExistsException contactAlreadyExistsException = Assertions.assertThrows(ContactAlreadyExistsException.class,() -> {
            throw new ContactAlreadyExistsException("POST","contactId","The given contact is already existing for the given Contact ID :2");
        });

        MessageHandlingException messageHandlingException = Assertions.assertThrows(MessageHandlingException.class,() -> {
            throw new MessageHandlingException(MessageBuilder.withPayload("The given contact is already existing for the given Contact ID :2").build(),contactAlreadyExistsException);
        });
        ErrorMessage errorMessage = new ErrorMessage(messageHandlingException);

        Message<Object> message = globalErrorHandler.handleError(errorMessage);
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getPayload());
        Assertions.assertEquals("The given contact is already existing for the given Contact ID :2",message.getPayload().toString());
        Assertions.assertNotNull(message.getHeaders());
        Assertions.assertEquals(4,message.getHeaders().size());
        Assertions.assertEquals(HttpStatus.CONFLICT,message.getHeaders().get("http_statusCode"));
    }

    @Test
    public void testHandleErrorForInterServerErrorWithStatusCode500(){
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();
        HttpServerErrorException httpServerErrorException = Assertions.assertThrows(HttpServerErrorException.class,() -> {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        ErrorMessage errorMessage = new ErrorMessage(httpServerErrorException);

        Message<Object> message = globalErrorHandler.handleError(errorMessage);
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getPayload());
        Assertions.assertEquals("The system is unable to process the request due to technical issue. Please try after sometime",message.getPayload().toString());
        Assertions.assertNotNull(message.getHeaders());
        Assertions.assertEquals(4,message.getHeaders().size());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,message.getHeaders().get("http_statusCode"));
    }
}
