package com.michael.spring.integration.address.book;

import com.michael.spring.integration.address.book.config.AddressBookConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AddressBookConfiguration.class)
@Slf4j
public class SpringIntegrationAddressBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationAddressBookApplication.class, args);
	}
}
