package com.michael.spring.integration.address.book.service;

import com.michael.spring.integration.address.book.entity.Contact;
import com.michael.spring.integration.address.book.exception.ContactAlreadyExistsException;
import com.michael.spring.integration.address.book.exception.ContactNotFoundException;
import com.michael.spring.integration.address.book.exception.InvalidContactIdException;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.model.response.ResponseDTO;
import com.michael.spring.integration.address.book.repository.ContactRepository;
import com.michael.spring.integration.address.book.service.implementation.ContactServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.michael.spring.integration.address.book.constants.ContactsConstant.CONTACT_DELETE_OPERATION_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ContactServiceTest {

    @InjectMocks
    ContactServiceImpl contactServiceImpl;

    @Mock
    ContactRepository contactRepository;

    @BeforeEach
     void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateContact(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);

        when(contactRepository.save(any())).thenReturn(contact);

        ContactDTO c1 = new ContactDTO();
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);

        Message result = contactServiceImpl.createContact(c1);
        Contact contactCreated = (Contact) result.getPayload();
        assertNotNull(contactCreated);

        assertNotNull(contactCreated.getContactId());
        assertEquals(contactId, contactCreated.getContactId());
        assertNotNull(contactCreated.getFullName());
        assertEquals(fullName, contactCreated.getFullName());
        assertNotNull(contactCreated.getEmailAddress());
        assertEquals(emailAddress, contactCreated.getEmailAddress());
        assertNotNull(contactCreated.getPhoneNumber());
        assertEquals(phoneNumber, contactCreated.getPhoneNumber());

        verify(contactRepository, times(1)).findByFullName(fullName);
        verify(contactRepository, times(1)).save(any());

    }

    @Test
     void testCreateContactForContactIdAlreadyExistingScenario(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));

        ContactDTO c1 = new ContactDTO();
        c1.setContactId(contactId);
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);

        ContactAlreadyExistsException contactAlreadyExistsException = Assertions.assertThrows(ContactAlreadyExistsException.class, ()-> {
            contactServiceImpl.createContact(c1);

        },"Expected ContactAlreadyExistsException to be thrown");
        assertNotNull(contactAlreadyExistsException);
        assertNotNull(contactAlreadyExistsException.getResourceName());
        assertNotNull(contactAlreadyExistsException.getFieldName());
        assertNotNull(contactAlreadyExistsException.getMessage());

        verify(contactRepository, times(1)).findById(contactId);
    }

    @Test
     void testCreateContactForContactNameAlreadyExistingScenario(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);

        when(contactRepository.findByFullName(fullName)).thenReturn(Arrays.asList(contact));

        ContactDTO c1 = new ContactDTO();
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);

        ContactAlreadyExistsException contactAlreadyExistsException = Assertions.assertThrows(ContactAlreadyExistsException.class, ()-> {
            contactServiceImpl.createContact(c1);

        },"Expected ContactAlreadyExistsException to be thrown");
        assertNotNull(contactAlreadyExistsException);
        assertNotNull(contactAlreadyExistsException.getResourceName());
        assertNotNull(contactAlreadyExistsException.getFieldName());
        assertNotNull(contactAlreadyExistsException.getMessage());
        verify(contactRepository, times(1)).findByFullName(fullName);
    }

    @Test
     void testUpdateContact(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";
        String phoneNumberTobeUpdated = "+919094523574";

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);

        Contact contactUpdated = new Contact();
        contactUpdated.setContactId(contactId);
        contactUpdated.setFullName(fullName);
        contactUpdated.setEmailAddress(emailAddress);
        contactUpdated.setPhoneNumber(phoneNumberTobeUpdated);


        Optional<Contact> optionalContact = Optional.of(contact);

        when(contactRepository.findById(any())).thenReturn(optionalContact);

        when(contactRepository.save(any())).thenReturn(contactUpdated);

        ContactDTO c1 = new ContactDTO();
        c1.setContactId(contactId);
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumberTobeUpdated);

        ContactDTO contactDTOUpdated = contactServiceImpl.updateContact(c1);
        assertNotNull(contactDTOUpdated);

        assertNotNull(contactDTOUpdated.getContactId());
        assertEquals(contactId, contactDTOUpdated.getContactId());
        assertNotNull(contactDTOUpdated.getFullName());
        assertEquals(fullName, contactDTOUpdated.getFullName());
        assertNotNull(contactDTOUpdated.getEmailAddress());
        assertEquals(emailAddress, contactDTOUpdated.getEmailAddress());
        assertNotNull(contactDTOUpdated.getPhoneNumber());
        assertEquals(phoneNumberTobeUpdated, contactDTOUpdated.getPhoneNumber());

        verify(contactRepository, times(1)).findById(contactId);
        verify(contactRepository, times(1)).save(any());
    }

    @Test
     void testContactNotFoundForUpdate(){

        Long contactId =  15L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        when(contactRepository.findById(contactId)).thenReturn(Optional.ofNullable(null));

        ContactDTO c1 = new ContactDTO();
        c1.setContactId(contactId);
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);

        ContactNotFoundException contactNotFoundException = Assertions.assertThrows(ContactNotFoundException.class, ()-> {
            contactServiceImpl.updateContact(c1);

        },"Expected ContactNotFoundException to be thrown");
        assertNotNull(contactNotFoundException);
        assertNotNull(contactNotFoundException.getResourceName());
        assertNotNull(contactNotFoundException.getFieldName());
        assertNotNull(contactNotFoundException.getMessage());
        verify(contactRepository, times(1)).findById(contactId);
    }


    @Test
     void testFindAllContacts(){

        List<Contact> list = new ArrayList<Contact>();

        final String ALFRED_MICHAEL = "Alfred Michael";
        Contact c1 = new Contact();
        c1.setContactId(1L);
        c1.setFullName("Michael Philomin");
        c1.setEmailAddress("michaelraj.p@gmail.com");
        c1.setPhoneNumber("+919094523573");
        list.add(c1);

        Contact c2 = new Contact();
        c2.setContactId(1L);
        c2.setFullName("Jenifer Christina");
        c2.setEmailAddress("jenichri.19.p@gmail.com");
        c2.setPhoneNumber("+919894067712");
        list.add(c2);


        Contact c3 = new Contact();
        c3.setContactId(1L);
        c3.setFullName(ALFRED_MICHAEL);
        c3.setEmailAddress("selvam181084@gmail.com");
        c3.setPhoneNumber("+919488251189");
        list.add(c3);

        Contact c4 = new Contact();
        c4.setContactId(1L);
        c4.setFullName("Priyanka Jeffin");
        c4.setEmailAddress("priyanka.jeffin@gmail.com");
        c4.setPhoneNumber("+919080053794");
        list.add(c4);

        when(contactRepository.findAll()).thenReturn(list);

        List<ContactDTO> contactDTOList = contactServiceImpl.getAllContacts();

        assertEquals(4, contactDTOList.size());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
     void testGetContactById(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);


        Optional<Contact> optionalContact = Optional.of(contact);

        when(contactRepository.findById(any())).thenReturn(optionalContact);

        Message<String> message = MessageBuilder.withPayload("1").setHeader("x-user-operation","Search Contacts By Name").build();


        ContactDTO contactDto = contactServiceImpl.getContactById(message);

        assertNotNull(contactDto.getContactId());

        assertNotNull(contactDto.getContactId());
        assertEquals(contactId, contactDto.getContactId());
        assertNotNull(contactDto.getFullName());
        assertEquals(fullName, contactDto.getFullName());
        assertNotNull(contactDto.getEmailAddress());
        assertEquals(emailAddress, contactDto.getEmailAddress());
        assertNotNull(contactDto.getPhoneNumber());
        assertEquals(phoneNumber, contactDto.getPhoneNumber());
    }

    @Test
     void testGetContactByIdForNotFoundScenario(){
        Long contactId =  18L;
        when(contactRepository.findById(contactId)).thenReturn(Optional.ofNullable(null));
        Message<String> message = MessageBuilder.withPayload("18").setHeader("x-user-operation","Search Contacts By Name").build();

        ContactNotFoundException contactNotFoundException = Assertions.assertThrows(ContactNotFoundException.class , () -> {
            contactServiceImpl.getContactById(message);
        },"Expected ContactNotFoundException to be thrown");
        assertNotNull(contactNotFoundException);
        assertNotNull(contactNotFoundException.getResourceName());
        assertNotNull(contactNotFoundException.getFieldName());
        assertNotNull(contactNotFoundException.getMessage());
    }
    @Test
     void testSearchContactsByName(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";

        List<Contact> list = new ArrayList<Contact>();
        Contact c1 = new Contact();
        c1.setContactId(contactId);
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);
        list.add(c1);

        when(contactRepository.findByFullName(fullName)).thenReturn(list);

        List<ContactDTO> contactDTOList = contactServiceImpl.searchContactsByName(fullName,"SearchByName",
                "Search Contacts By Name");
        assertNotNull(contactDTOList);
        assertEquals(1, contactDTOList.size());
        ContactDTO contactDTO = contactDTOList.get(0);
        assertNotNull(contactDTO.getContactId());
        assertEquals(contactId, contactDTO.getContactId());
        assertNotNull(contactDTO.getFullName());
        assertEquals(fullName, contactDTO.getFullName());
        assertNotNull(contactDTO.getEmailAddress());
        assertEquals(emailAddress, contactDTO.getEmailAddress());
        assertNotNull(contactDTO.getPhoneNumber());
        assertEquals(phoneNumber, contactDTO.getPhoneNumber());
    }

    @Test
     void testSearchContactsByNameForNotFoundScenario(){
        String fullName = "Michael Philomin";
        when(contactRepository.findByFullName(fullName)).thenReturn(null);

        ContactNotFoundException contactNotFoundException = Assertions.assertThrows(ContactNotFoundException.class, ()->{
            contactServiceImpl.searchContactsByName(fullName,"SearchByName",
                    "Search Contacts By Name");
        },"Expected ContactNotFoundException to be thrown");
        assertNotNull(contactNotFoundException);
        assertNotNull(contactNotFoundException.getResourceName());
        assertNotNull(contactNotFoundException.getFieldName());
        assertNotNull(contactNotFoundException.getMessage());
    }


    @Test
     void testDeleteContact(){

        Long contactId =  1L;
        String fullName = "Michael Philomin";
        String emailAddress = "michaelraj.p@gmail.com";
        String phoneNumber = "+919094523573";
        String messageForResponse = String.format(CONTACT_DELETE_OPERATION_MSG,contactId);

        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFullName(fullName);
        contact.setEmailAddress(emailAddress);
        contact.setPhoneNumber(phoneNumber);

        Optional<Contact> optionalContact = Optional.of(contact);
        when(contactRepository.findById(any())).thenReturn(optionalContact);

        Message<String> message = MessageBuilder.withPayload(contactId.toString()).build();
        ResponseDTO responseDTO = contactServiceImpl.deleteContact(message);
        assertNotNull(responseDTO);
        assertEquals(messageForResponse , responseDTO.getMessage());
    }

    @Test
     void testDeleteContactForNotFoundScenario(){
        Long contactId =  1L;
        when(contactRepository.findById(contactId)).thenReturn(Optional.ofNullable(null));

        Message<String> message = MessageBuilder.withPayload(contactId.toString()).build();
        ContactNotFoundException contactNotFoundException = Assertions.assertThrows(ContactNotFoundException.class,()->{
            contactServiceImpl.deleteContact(message);
        },"Expected ContactNotFoundException to be thrown");
        assertNotNull(contactNotFoundException);
        assertNotNull(contactNotFoundException.getResourceName());
        assertNotNull(contactNotFoundException.getFieldName());
        assertNotNull(contactNotFoundException.getMessage());
    }

    @Test
     void testDeleteContactForInvalidContactId(){
        Message<String> message = MessageBuilder.withPayload("").build();
        InvalidContactIdException invalidContactIdException = Assertions.assertThrows(InvalidContactIdException.class,()->{
            contactServiceImpl.deleteContact(message);
        },"Expected InvalidContactIdException to be thrown");
        assertNotNull(invalidContactIdException);
        assertNotNull(invalidContactIdException.getFieldName());
        assertNotNull(invalidContactIdException.getMessage());
    }

    @Test
     void testDeleteContactForNumberFormatException(){
        Message<String> message = MessageBuilder.withPayload("Michael").build();
        InvalidContactIdException invalidContactIdException = Assertions.assertThrows(InvalidContactIdException.class,()->{
            contactServiceImpl.deleteContact(message);
        },"Expected InvalidContactIdException to be thrown");
        assertNotNull(invalidContactIdException);
        assertNotNull(invalidContactIdException.getFieldName());
        assertNotNull(invalidContactIdException.getMessage());
    }
}
