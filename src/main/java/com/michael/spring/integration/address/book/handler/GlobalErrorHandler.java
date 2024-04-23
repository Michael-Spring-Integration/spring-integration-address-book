package com.michael.spring.integration.address.book.handler;

import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import com.michael.spring.integration.address.book.util.ContactsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GlobalErrorHandler {
    @ServiceActivator(inputChannel = "httpErrorChannel", outputChannel = "httpReplyChannel")
    public Message<Object> handleError(ErrorMessage message) {
        Map<String,Object> customHeaders = new HashMap<>();
        customHeaders.put(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String errorMessage = null;
        if(message.getPayload().getCause() instanceof InvalidContactDetailsException invalidContactDetailsException) {
            List<ObjectError> objectErrors = invalidContactDetailsException.getObjectErrors();
            log.error("The exception thrown while validating the message : {} ", objectErrors);
            errorMessage = objectErrors.get(0).getDefaultMessage();
        }else if (message.getPayload() instanceof MessageHandlingException){
            errorMessage = message.getPayload().getCause().getMessage();
            log.error("The exception thrown while processing message : {} ", errorMessage);
        }
        HttpStatus httpStatus = getHttpStatus(message);
        customHeaders.put(HttpHeaders.STATUS_CODE, httpStatus);
        return MessageBuilder.createMessage(errorMessage, ContactsUtil.createMessageHeaders(customHeaders));
    }

    private HttpStatus getHttpStatus(ErrorMessage message) {
        HttpStatus httpStatus = null;
        switch (message.getPayload().getCause().getClass().getSimpleName()) {
            case "ContactNotFoundException":
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case "InvalidContactIdException","InvalidContactDetailsException":
                log.error("The exception thrown for invalid contact details : {} ", message.getPayload().getCause());
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case "ContactAlreadyExistsException":
                httpStatus = HttpStatus.CONFLICT;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        return httpStatus;
    }
}
