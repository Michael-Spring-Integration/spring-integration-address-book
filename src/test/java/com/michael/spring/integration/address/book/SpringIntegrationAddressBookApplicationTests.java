package com.michael.spring.integration.address.book;

import com.michael.spring.integration.address.book.repository.ContactRepository;
import com.michael.spring.integration.address.book.service.ContactService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringIntegrationAddressBookApplicationTests {

	@Autowired
	ContactService contactService;

	@Autowired
	ContactRepository contactRepository;

	@Test
	void contextLoads() {
		Assertions.assertThat(contactRepository).isNotEqualTo(null);
		Assertions.assertThat(contactService).isNotEqualTo(null);
	}

}
