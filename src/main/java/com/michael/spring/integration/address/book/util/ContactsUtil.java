package com.michael.spring.integration.address.book.util;

import org.springframework.http.HttpStatus;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

public class ContactsUtil {

    private ContactsUtil(){

    }
    public static MessageHeaders createMessageHeaders(Map<String,Object> customHeaders){
        return new MessageHeaders(customHeaders);
    }

    public static Map<String,Object> createHeaderMap(HttpStatus httpStatus){
        Map<String,Object> customHeadersMap = new HashMap<>();
        customHeadersMap.put(HttpHeaders.STATUS_CODE, httpStatus);
        return new HashMap<>();
    }
}
