package com.michael.spring.integration.address.book.repository;

import com.michael.spring.integration.address.book.entity.Contact;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
 class ContactRepositoryTest {

    // Alternative for EntityManager
    // Optional in this case, we can use bookRepository to do the same stuff
    @Autowired
    private TestEntityManager testEM;

    @Autowired
    ContactRepository contactRepository;
    @Test
     void testFindByFullName(){

        String fullName = "Michael Philomin";
        String emailId = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";
        Contact c1 = new Contact();
        c1.setFullName(fullName);
        c1.setEmailAddress(emailId);
        c1.setPhoneNumber(phoneNumber);

        contactRepository.save(c1);

        List<Contact> contactList = contactRepository.findByFullName(c1.getFullName());

        assertNotNull(contactList);
        Contact result = contactList.get(0);
        assertNotNull(contactList);

        assertNotNull(result.getContactId());
        assertEquals(1L,result.getContactId());

        assertNotNull(result.getFullName());
        assertEquals(fullName,result.getFullName());

        assertNotNull(result.getEmailAddress());
        assertEquals(emailId,result.getEmailAddress());

        assertNotNull(result.getPhoneNumber());
        assertEquals(phoneNumber,result.getPhoneNumber());

    }

}
