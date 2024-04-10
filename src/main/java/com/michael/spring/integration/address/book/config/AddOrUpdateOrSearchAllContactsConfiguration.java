package com.michael.spring.integration.address.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;

@Configuration
public class AddOrUpdateOrSearchAllContactsConfiguration {
    @Autowired
    MessageChannel httpRequestChannel;
    @Autowired
    MessageChannel httpReplyChannel;
    @Autowired
    MessageChannel httpErrorChannel;
    @Bean
    public HttpRequestHandlingMessagingGateway httpGatewayForAddOrUpdateOrSearchAllContacts() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForAddOrUpdateOrSearchAllContacts());
        gateway.setRequestChannel(httpRequestChannel);
        gateway.setReplyChannel(httpReplyChannel);
        gateway.setErrorChannel(httpErrorChannel);
        return gateway;
    }
    private RequestMapping createRequestMappingForAddOrUpdateOrSearchAllContacts() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT); // Add more methods as needed
        requestMapping.setPathPatterns("/contacts/add", "/contacts/update","contacts/search","contacts/search-by-name/*"); // Add more paths as needed
        return requestMapping;
    }
    @Bean
    public MessageChannel searchAllContactsChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel addContactChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel updateContactChannel() {
        return new DirectChannel();
    }

}
