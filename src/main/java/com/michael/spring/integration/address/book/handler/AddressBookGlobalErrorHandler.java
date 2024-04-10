package com.michael.spring.integration.address.book.handler;

import com.michael.spring.integration.address.book.exception.ContactAlreadyExistsException;
import com.michael.spring.integration.address.book.exception.ContactNotFoundException;
import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import com.michael.spring.integration.address.book.exception.InvalidContactIdException;
import com.michael.spring.integration.address.book.util.ContactsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AddressBookGlobalErrorHandler {
    @ServiceActivator(inputChannel = "httpErrorChannel", outputChannel = "httpReplyChannel")
    public Message<String> handleError(Message<MessageHandlingException> message) {
        Map<String,Object> customHeaders = new HashMap<>();
        String errorMessage =  message.getPayload().getCause().getMessage();
        log.error("The exception thrown while processing message : {} ",errorMessage);
        HttpStatus httpStatus = getHttpStatus(message);
        customHeaders.put(HttpHeaders.STATUS_CODE, httpStatus);
        return MessageBuilder.createMessage(errorMessage, ContactsUtil.createMessageHeaders(customHeaders));
    }

    private HttpStatus getHttpStatus(Message<MessageHandlingException> message) {
        HttpStatus httpStatus = null;
        if (message.getPayload().getCause() instanceof ContactNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        }else if(message.getPayload().getCause() instanceof InvalidContactIdException){
            httpStatus = HttpStatus.BAD_REQUEST;
        }else if(message.getPayload().getCause() instanceof InvalidContactDetailsException){
            httpStatus = HttpStatus.BAD_REQUEST;
        }else if(message.getPayload().getCause() instanceof ContactAlreadyExistsException){
            httpStatus = HttpStatus.CONFLICT;
        }else if(message.getPayload().getCause() instanceof Exception){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return httpStatus;
    }
}
