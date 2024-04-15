package com.michael.spring.integration.address.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class SearchAllContactsConfiguration {

    @Bean
    public HttpRequestHandlingMessagingGateway httpGatewayForSearchAllContacts(MessageChannel httpRequestChannel,
                                                                               MessageChannel httpReplyChannel,
                                                                               MessageChannel httpErrorChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForSearchAllContacts());
        gateway.setRequestChannel(httpRequestChannel);
        gateway.setReplyChannel(httpReplyChannel);
        gateway.setErrorChannel(httpErrorChannel);
        return gateway;
    }

    private RequestMapping createRequestMappingForSearchAllContacts() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.GET); // Add more methods as needed
        requestMapping.setPathPatterns("contacts/search"); // Add more paths as needed
        return requestMapping;
    }

    @Bean
    public MessageChannel searchAllContactsChannel() {
        return new DirectChannel();
    }
}
