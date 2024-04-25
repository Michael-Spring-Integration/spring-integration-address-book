package com.michael.spring.integration.address.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;

@Configuration
public class GetAndDeleteContactConfiguration {

    @Bean
    public HttpRequestHandlingMessagingGateway gatewayForGetAndDeleteContact(MessageChannel httpRequestChannel,
                                                                                MessageChannel httpReplyChannel,
                                                                                MessageChannel httpErrorChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForGetOrDeleteContact());
        gateway.setRequestChannel(httpRequestChannel);
        gateway.setReplyChannel(httpReplyChannel);
        gateway.setErrorChannel(httpErrorChannel);
        gateway.setPayloadExpression(createPayloadExpressionForGetOrDeleteContact());
        return gateway;
    }
    private RequestMapping createRequestMappingForGetOrDeleteContact() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.GET, HttpMethod.DELETE); // Add more methods as needed
        requestMapping.setPathPatterns("/contacts/get/{contactId}", "/contacts/delete/{contactId}"); // Add more paths as needed
        return requestMapping;
    }
    private Expression createPayloadExpressionForGetOrDeleteContact(){
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression("#pathVariables.contactId");
    }

    @Bean
    public MessageChannel deleteContactChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel getContactChannel() {
        return new DirectChannel();
    }

}
