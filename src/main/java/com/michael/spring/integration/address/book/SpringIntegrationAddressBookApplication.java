package com.michael.spring.integration.address.book;

import com.michael.spring.integration.address.book.config.AddressBookConfiguration;
import com.michael.spring.integration.address.book.model.response.ResponseDTO;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;

@SpringBootApplication
@Import(AddressBookConfiguration.class)
@Slf4j
public class SpringIntegrationAddressBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationAddressBookApplication.class, args);
	}

	// Spring runs CommandLineRunner bean when Spring Boot App starts
	/*@Bean
	public CommandLineRunner demo(ContactRepository contactRepository) {
		return (args) -> {

			Contact c1 = new Contact();
			c1.setFullName("Michael Philomin");
			c1.setEmailAddress("michaelraj.p@gmail.com");
			c1.setPhoneNumber("+919094523573");

			Contact c2 = new Contact();
			c2.setFullName("Jenifer Christina");
			c2.setEmailAddress("jenichri.19.p@gmail.com");
			c2.setPhoneNumber("+919894067712");


			Contact c3 = new Contact();
			c3.setFullName("Alfred Michael");
			c3.setEmailAddress("selvam181084@gmail.com");
			c3.setPhoneNumber("+919488251189");

			Contact c4 = new Contact();
			c4.setFullName("Priyanka Jeffin");
			c4.setEmailAddress("priyanka.jeffin@gmail.com");
			c4.setPhoneNumber("+919080053794");

			// save a few contacts, ID auto increase, expect 1, 2, 3, 4
			contactRepository.save(c1);
			contactRepository.save(c2);
			contactRepository.save(c3);
			contactRepository.save(c4);


			// find all contacts
			log.info("findAll(), expect 3 contacts");
			log.info("-------------------------------");
			for (Contact contact : contactRepository.findAll()) {
				log.info(contact.toString());
			}
			log.info("\n");

			// find contact by ID
			Optional<Contact> optionalContact = contactRepository.findById(1L);
			optionalContact.ifPresent(obj -> {
				log.info("Contact found with findById(1L):");
				log.info("--------------------------------");
				log.info(obj.toString());
				log.info("\n");
			});

			// find Contact by full name
			log.info("Contact found with findByFullName('Alfred Michael')");
			log.info("--------------------------------------------");
			contactRepository.findByFullName("Alfred Michael").forEach(c -> {
				log.info(c.toString());
				log.info("\n");
			});

			// delete a contact
			contactRepository.deleteById(2L);
			log.info("Contact deleted where ID = 2L");
			log.info("--------------------------------------------");

			// find all contacts
			log.info("findAll() again, expect 2 contacts");
			log.info("-----------------------------------");
			for (Contact contact : contactRepository.findAll()) {
				log.info(contact.toString());
			}
			log.info("\n");

		};
	}*/

	@Bean
	public CommandLineRunner demo(ContactService contactService) {
		return args -> {

			final String ALFRED_MICHAEL = "Alfred Michael";
			ContactDTO c1 = new ContactDTO();
			c1.setFullName("Michael Philomin");
			c1.setEmailAddress("michaelraj.p@gmail.com");
			c1.setPhoneNumber("+919094523573");

			ContactDTO c2 = new ContactDTO();
			c2.setFullName("Jenifer Christina");
			c2.setEmailAddress("jenichri.19.p@gmail.com");
			c2.setPhoneNumber("+919894067712");


			ContactDTO c3 = new ContactDTO();
			c3.setFullName(ALFRED_MICHAEL);
			c3.setEmailAddress("selvam181084@gmail.com");
			c3.setPhoneNumber("+919488251189");

			ContactDTO c4 = new ContactDTO();
			c4.setFullName("Priyanka Jeffin");
			c4.setEmailAddress("priyanka.jeffin@gmail.com");
			c4.setPhoneNumber("+919080053794");

			// Create a few contacts, ID auto increase, expect 1, 2, 3, 4
			contactService.createContact(c1);
			contactService.createContact(c2);
			contactService.createContact(c3);
			contactService.createContact(c4);

			// Get all contacts
			log.info("findAll(), expect 4 contacts");
			log.info("-------------------------------");
			log.info("The loaded contact details");
			List<ContactDTO> contactDTOList = contactService.getAllContacts();
			contactDTOList.forEach(contactDTO -> log.info(contactDTO.toString()));
			log.info("\n");

			// Get contact by ID
			Message<String> message = MessageBuilder.withPayload("1").build();
			ContactDTO contactDTOFetched = contactService.getContactById(message);
			log.info("Contact found with findById(1L):");
			log.info("--------------------------------");
			log.info(contactDTOFetched.toString());
			log.info("\n");


			// Update the contact
			ContactDTO nameUpdate = new ContactDTO();
			nameUpdate.setContactId(1L);
			nameUpdate.setFullName("Michael Philomin Raj");
			nameUpdate.setEmailAddress("michaelraj.p@gmail.com");
			nameUpdate.setPhoneNumber("+919094523573");
			ContactDTO contactUpdated = contactService.updateContact(nameUpdate);
			log.info("Contact updated by Contact Id:");
			log.info("--------------------------------");
			log.info(contactUpdated.toString());
			log.info("\n");

			// Get the contacts by full name
			log.info("Contact found with findByFullName('Alfred Michael')");
			log.info("--------------------------------------------");
			contactService.searchContactsByName(ALFRED_MICHAEL,"Search Contacts By Name", "SearchByName").forEach(c -> {
				log.info(c.toString());
				log.info("\n");
			});

			// Delete a contact
			Message<String> messageForDelete = MessageBuilder.withPayload("4").build();
			ResponseDTO resultOfDeleteOperation = contactService.deleteContact(messageForDelete);
			log.info("Contact deleted where ID = 2L");
			log.info("The result of Delete operation is {} " , resultOfDeleteOperation);

			log.info("--------------------------------------------");

			// Get all the contacts
			log.info("findAll() again, expect 3 contacts");
			log.info("-----------------------------------");
			log.info("The loaded contact details");
			List<ContactDTO> contactDTOList1 = contactService.getAllContacts();
			contactDTOList1.forEach(contactDTO -> log.info(contactDTO.toString()));
			log.info("\n");

		};
	}


}
