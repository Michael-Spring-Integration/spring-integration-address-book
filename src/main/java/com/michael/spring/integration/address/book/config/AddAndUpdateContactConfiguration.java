package com.michael.spring.integration.address.book.config;

import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.validator.PayloadValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;
import org.springframework.validation.Validator;

@Configuration
public class AddAndUpdateContactConfiguration {

    @Bean
    public HttpRequestHandlingMessagingGateway gatewayForAddAndUpdateContact(MessageChannel httpRequestChannel,
                                                                                MessageChannel httpReplyChannel,
                                                                                MessageChannel httpErrorChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForAddOrUpdateContact());
        gateway.setRequestChannel(httpRequestChannel);
        gateway.setReplyChannel(httpReplyChannel);
        gateway.setErrorChannel(httpErrorChannel);
        gateway.setRequestPayloadTypeClass(ContactDTO.class);
        gateway.setValidator(validator());
        gateway.setAutoStartup(true);
        return gateway;
    }

    private RequestMapping createRequestMappingForAddOrUpdateContact() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.POST, HttpMethod.PUT); // Add more methods as needed
        requestMapping.setPathPatterns("/contacts/add", "/contacts/update"); // Add more paths as needed
        return requestMapping;
    }

    @Bean
    public Validator validator() {
        return new PayloadValidator();
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
