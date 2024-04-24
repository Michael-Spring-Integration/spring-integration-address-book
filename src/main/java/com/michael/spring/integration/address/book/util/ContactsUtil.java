package com.michael.spring.integration.address.book.util;

import org.springframework.messaging.MessageHeaders;

import java.util.Map;

public class ContactsUtil {

    private ContactsUtil(){

    }
    public static MessageHeaders createMessageHeaders(Map<String,Object> customHeaders){
        return new MessageHeaders(customHeaders);
    }
}
