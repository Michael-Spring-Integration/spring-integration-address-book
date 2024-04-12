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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SearchContactByNameConfiguration {
    @Bean
    public HttpRequestHandlingMessagingGateway httpGatewayForSearchContactsByName(MessageChannel httpRequestChannel,
                                                                                  MessageChannel httpReplyChannel,
                                                                                  MessageChannel httpErrorChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForSearchContactsByName());
        gateway.setRequestChannel(httpRequestChannel);
        gateway.setReplyChannel(httpReplyChannel);
        gateway.setErrorChannel(httpErrorChannel);
        gateway.setPayloadExpression(createPayloadExpressionForSearchContactsByName());
        gateway.setHeaderExpressions(createHeaderExpressionForSearchContactsByName());
        return gateway;
    }
    private RequestMapping createRequestMappingForSearchContactsByName() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.GET); // Add more methods as needed
        requestMapping.setPathPatterns("contacts/searchByName/{contactName}"); // Add more paths as needed
        return requestMapping;
    }

    private Expression createPayloadExpressionForSearchContactsByName(){
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression("#pathVariables.contactName");
    }
    private Map<String,Expression> createHeaderExpressionForSearchContactsByName(){
        Map<String,Expression> headerExpression = new HashMap<>();
        ExpressionParser parser = new SpelExpressionParser();
        Expression operationNameInCustomHeader = parser.parseExpression("#requestHeaders['x-user-operation']");
        Expression operationNameInRequestParameter = parser.parseExpression("#requestParams.operationName");
        headerExpression.put("operationNameInCustomHeader",operationNameInCustomHeader);
        headerExpression.put("operationNameInRequestParameter",operationNameInRequestParameter);
        return headerExpression;
    }

    @Bean
    public MessageChannel searchContactsByNameChannel() {
        return new DirectChannel();
    }
}
