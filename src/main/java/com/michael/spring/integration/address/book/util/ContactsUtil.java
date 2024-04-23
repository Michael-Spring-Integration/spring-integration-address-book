package com.michael.spring.integration.address.book.util;

import org.springframework.http.HttpStatus;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

public class ContactsUtil {
    public static MessageHeaders createMessageHeaders(Map<String,Object> customHeaders){
        return new MessageHeaders(customHeaders);
    }
}
