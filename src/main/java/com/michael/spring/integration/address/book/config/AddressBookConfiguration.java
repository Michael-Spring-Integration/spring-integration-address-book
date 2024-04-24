package com.michael.spring.integration.address.book.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
@EnableIntegration
@Import({GetAndDeleteContactConfiguration.class,
        SearchContactsByNameConfiguration.class,
        AddAndUpdateContactConfiguration.class})
@Slf4j
public class AddressBookConfiguration {

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

                    log.debug("The requestMessage {} " , requestMessage);

                    //requestMessage.getHeaders().forEach((key, value) -> log.info("{}: {}", key, value));

                    String method = requestMessage.getHeaders().get(HttpHeaders.REQUEST_METHOD, String.class);
                    log.debug("The Requested HTTP Method  : {} " , method);

                    String path = requestMessage.getHeaders().get(HttpHeaders.REQUEST_URL, String.class);
                    log.debug("The Requested HTTP Path  : {} " , path);

                    if (HttpMethod.POST.toString().equals(method) && path.contains("/contacts/add")) {
                        return "addContactChannel";
                    } else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/get/")) {
                        return "getContactChannel";
                    }else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/searchByName")) {
                        return "searchContactsByNameChannel"; // Handle other cases as needed
                    }else if (HttpMethod.GET.toString().equals(method) && path.contains("/contacts/search")) {
                        return "searchAllContactsChannel"; // Handle other cases as needed
                    } else if (HttpMethod.DELETE.toString().equals(method) && path.contains("/contacts/delete")) {
                        return "deleteContactChannel";
                    } else {
                        return "updateContactChannel";
                    }
                })
                .get();
    }
}


