package com.michael.spring.integration.address.book.service;

import com.michael.spring.integration.address.book.model.request.ContactDTO;
import org.springframework.messaging.Message;

import java.util.List;

public interface ContactService {

    Message createContact(ContactDTO contactDTO);
    Message updateContact(ContactDTO contactDTO);
    List<ContactDTO> getAllContacts();
    Message getContactById(Message message);
    List<ContactDTO> searchContactsByName(String contactName, String operationNameByRequestParam, String operationNameByRequestHeader);
    Message deleteContact(Message message);
}
