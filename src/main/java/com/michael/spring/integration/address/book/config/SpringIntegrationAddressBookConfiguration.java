package com.michael.spring.integration.address.book.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableIntegration
@Import({GetOrDeleteContactConfiguration.class,
        SearchContactByNameConfiguration.class,
        AddOrUpdateOrSearchAllContactsConfiguration.class})
@Slf4j
public class SpringIntegrationAddressBookConfiguration {

    @Bean
    public MessageChannel httpRequestChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageChannel httpReplyChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel httpErrorChannel() {
        return new DirectChannel();
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
}


