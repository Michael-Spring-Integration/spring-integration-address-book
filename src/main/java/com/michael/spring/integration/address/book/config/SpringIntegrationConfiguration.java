package com.michael.spring.integration.address.book.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.mapping.InboundMessageMapper;
import org.springframework.integration.mapping.OutboundMessageMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableIntegration
@Slf4j
public class SpringIntegrationConfiguration {

   /*  @Bean
    public MessageChannel addContactHttpRequestChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel addContactHttpResponseChannel() {
        return new DirectChannel();
    }
   @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlow.from(httpRequestChannel())
                .channel(httpResponseChannel())
                .get();
    }*/

   /* @Bean
    public HttpRequestHandlingMessagingGateway addContactHttpGateway() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(mappingForAddContact());
        //gateway.setRequestPayloadType(String.class);
        gateway.setRequestChannel(addContactHttpRequestChannel());
        gateway.setReplyChannel(addContactHttpResponseChannel());
        gateway.setReplyTimeout(5000);
        return gateway;
    }*/

  /*  @Bean
    public RequestMapping mappingForAddContact() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setPathPatterns("/contacts/add");
        requestMapping.setMethods(HttpMethod.POST);
        return requestMapping;
    }*/

    @Bean
    public MessageChannel httpRequestChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel httpReplyChannel() {
        return new DirectChannel();
    }

    @Bean
    public HttpRequestHandlingMessagingGateway httpGatewayForGetOrDeleteContact() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForGetOrDeleteContact());
        gateway.setRequestChannel(httpRequestChannel());
        gateway.setReplyChannel(httpReplyChannel());
        gateway.setPayloadExpression(createPayloadExpressionForGetOrDeleteContact());
       // gateway.setRequestMapper(createRequestMapperForGetContact());
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

/*    private InboundMessageMapper<?> createRequestMapperForGetContact(){
        return new InboundMessageMapper<Object>() {
            @Override
            public Message<?> toMessage(Object object, Map<String, Object> headers) {
                log.info("The logging the headers in createReplyMapperForGetContact()");
                Message message = null;
                headers.forEach((k,v) -> log.info("{} {} " , k,v));
                if (headers.containsKey(HttpHeaders.STATUS_CODE)) {
                    int statusCode = (int) headers.get("customStatusCode");
                    log.info("The custom HTTP response status code to be sent to client : {}" , statusCode);
                    message = MessageBuilder.withPayload(object).build();
                    message = MessageBuilder.fromMessage(message)
                            .setHeader(HttpHeaders.STATUS_CODE, statusCode)
                            .build();
                }
                return message;
            }
        };
    }*/

    @Bean
    public HttpRequestHandlingMessagingGateway httpGatewayForSearchContactsByName() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForSearchContactsByName());
        gateway.setRequestChannel(httpRequestChannel());
        gateway.setReplyChannel(httpReplyChannel());
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
    public HttpRequestHandlingMessagingGateway httpGatewayForAddOrUpdateOrSearchAllContacts() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);
        gateway.setRequestMapping(createRequestMappingForAddOrUpdateOrSearchAllContacts());
        gateway.setRequestChannel(httpRequestChannel());
        gateway.setReplyChannel(httpReplyChannel());
        //gateway.setReplyMapper(createReplyMapperForAddContacts());
        return gateway;
    }
    private RequestMapping createRequestMappingForAddOrUpdateOrSearchAllContacts() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT); // Add more methods as needed
        requestMapping.setPathPatterns("/contacts/add", "/contacts/update","contacts/search","contacts/search-by-name/*"); // Add more paths as needed
        return requestMapping;
    }

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlow.from("httpRequestChannel")
                .route(Message.class, requestMessage -> {
                    log.info("The requestMessage {} " , requestMessage);
                    log.info("The values of all (both standard and custom) http headers from the message : ");
                    requestMessage.getHeaders().forEach((key, value) -> log.info("{}: {}", key, value));

                    String method = requestMessage.getHeaders().get(HttpHeaders.REQUEST_METHOD, String.class);
                    log.info("The Requested HTTP Method  : {} " , method);

                    String path = requestMessage.getHeaders().get(HttpHeaders.REQUEST_URL, String.class);
                    log.info("The Requested HTTP Path  : {} " , path);

                    if (HttpMethod.POST.toString().equals(method) && path.contains("/contacts/add")) {
                        log.info("The method call in ContactServiceImpl -> createContact() ");
                        return "addContactChannel";
                    } else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/get/")) {
                        log.info("The method call in ContactServiceImpl -> getContactById() ");
                        return "getContactChannel";
                    }else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/searchByName")) {
                        log.info("The method call in ContactServiceImpl -> searchContactsByName() ");
                        return "searchContactsByNameChannel"; // Handle other cases as needed
                    }else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/search")) {
                        log.info("The method call in ContactServiceImpl -> getAllContacts() ");
                        return "searchAllContactsChannel"; // Handle other cases as needed
                    } else if (HttpMethod.DELETE.toString().equals(method) && path.contains("/contacts/delete")) {
                        log.info("The method call in ContactServiceImpl -> deleteContact() ");
                        return "deleteContactChannel";
                    } else {
                        return "updateContactChannel";
                    }
                })
                .get();
    }

    @Bean
    public MessageChannel getContactChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel searchContactsByNameChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel searchAllContactsChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel deleteContactChannel() {
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


