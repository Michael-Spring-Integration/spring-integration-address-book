package com.michael.spring.integration.address.book.service;

import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.model.response.ResponseDTO;
import org.springframework.messaging.Message;

import java.util.List;

public interface ContactService {

    Message<ContactDTO> createContact(ContactDTO contactDTO);
    ContactDTO updateContact(ContactDTO contactDTO);
    List<ContactDTO> getAllContacts();
    ContactDTO getContactById(Message<String> message);
    List<ContactDTO> searchContactsByName(String contactName, String operationNameByRequestParam, String operationNameByRequestHeader);
    ResponseDTO deleteContact(Message<String> message);
}
