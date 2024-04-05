package com.michael.spring.integration.address.book.service.implementation;

import com.michael.spring.integration.address.book.constants.ContactsConstant;
import com.michael.spring.integration.address.book.entity.Contact;
import com.michael.spring.integration.address.book.exception.ContactNotFoundException;
import com.michael.spring.integration.address.book.mapper.ContactMapper;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.model.response.ResponseDTO;
import com.michael.spring.integration.address.book.repository.ContactRepository;
import com.michael.spring.integration.address.book.service.ContactService;
import com.michael.spring.integration.address.book.util.ContactsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ContactServiceImpl implements ContactService {
    ContactRepository contactRepository;
    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    @Override
    @ServiceActivator(inputChannel = "addContactChannel", outputChannel = "httpReplyChannel")
    public Message createContact(ContactDTO contactDTO) {
        log.info("The contact details received : {} ", contactDTO);
        Contact contactToBeCreated = ContactMapper.mapToContact(contactDTO);
        Contact contactCreated = contactRepository.save(contactToBeCreated);
        Map<String,Object> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.STATUS_CODE, HttpStatus.CREATED);
        return MessageBuilder.createMessage(contactCreated,ContactsUtil.createMessageHeaders(customHeaders));
    }

    @Override
    @ServiceActivator(inputChannel = "updateContactChannel", outputChannel = "httpReplyChannel")
    public Message updateContact(ContactDTO contactDTO) {
        Optional<Contact> contactDb = this.contactRepository.findById(contactDTO.getContactId());
        if (contactDb.isPresent()) {
            Contact contactUpdate = contactDb.get();
            contactUpdate.setFullName(contactDTO.getFullName());
            contactUpdate.setEmailAddress(contactDTO.getEmailAddress());
            contactUpdate.setPhoneNumber(contactDTO.getPhoneNumber());
            contactRepository.save(contactUpdate);
            return MessageBuilder.withPayload(ContactMapper.mapToContactDTO(contactUpdate)).build();
        } else {
            String detailMessage = ContactsConstant.CONTACT_NOT_FOUND + contactDTO.getContactId();
            Map<String,Object> customHeaders = new HashMap<>();
            customHeaders.put(HttpHeaders.STATUS_CODE, HttpStatus.NOT_FOUND);
            return MessageBuilder.createMessage(detailMessage,ContactsUtil.createMessageHeaders(customHeaders));        }
    }

    @Override
    @ServiceActivator(inputChannel = "searchAllContactsChannel", outputChannel = "httpReplyChannel")
    public List<ContactDTO> getAllContacts() {
        List<Contact> contactList = contactRepository.findAll();
        return ContactMapper.mapToContactDTOs(contactList);
    }

    @Override
    @ServiceActivator(inputChannel = "getContactChannel", outputChannel = "httpReplyChannel")
    public Message getContactById(Message message) {
        if (message != null && message.getHeaders() != null) {
            log.info("The values of all http headers from the message : ");
            message.getHeaders().forEach((key, value) -> log.info("{}: {}", key, value));
        }

        long contactId = Long.parseLong(message.getPayload().toString());
        log.info("The contactId received in getContactById() : {} ", contactId);
        try {
            Optional<Contact> contactDb = contactRepository.findById(contactId);
            if (contactDb.isPresent()) {
                ContactDTO contactDTO = ContactMapper.mapToContactDTO(contactDb.get());
                return MessageBuilder.withPayload(contactDTO).build();
            }else {
                String detailMessage = ContactsConstant.CONTACT_NOT_FOUND + contactId;
                throw new ContactNotFoundException("Post", "contactId", contactId,detailMessage);
            }
        }catch (ContactNotFoundException contactNotFoundException){
            log.error("The contact not found." + " Details : {} " ,contactNotFoundException.getMessage());
            //return MessageBuilder.withPayload(contactNotFoundException).build();
            Map<String,Object> customHeaders = new HashMap<>();
            customHeaders.put(HttpHeaders.STATUS_CODE, HttpStatus.NOT_FOUND);
            return MessageBuilder.createMessage(contactNotFoundException.getMessage(),ContactsUtil.createMessageHeaders(customHeaders));
        }
    }

    @Override
    @ServiceActivator(inputChannel = "searchContactsByNameChannel", outputChannel = "httpReplyChannel")
    public List<ContactDTO> searchContactsByName(@Payload String contactName,
                                                 @Header("operationNameInRequestParameter") String operationNameByRequestParam,
                                                 @Header("operationNameInCustomHeader") String operationNameByRequestHeader) {
        log.info("The contactName received in searchContactsByName() : {} ", contactName);
        log.info("The operationName received in searchContactsByName() via Request or Query Parameter : {} ", operationNameByRequestParam);
        log.info("The operationName received in searchContactsByName() via Request Custom Request Header : {} ", operationNameByRequestHeader);
        List<Contact> contactList = contactRepository.findByFullName(contactName);
        return ContactMapper.mapToContactDTOs(contactList);
    }

    @Override
    @ServiceActivator(inputChannel = "deleteContactChannel", outputChannel = "httpReplyChannel")
    public Message deleteContact(Message message) {
        long contactId = Long.parseLong(message.getPayload().toString());
        log.info("The contactId received in deleteContact() : {} ", contactId);
        Optional<Contact> contactDb = contactRepository.findById(contactId);
        try {
            if (contactDb.isPresent()) {
                contactRepository.delete(contactDb.get());
                String messageForResponse = "Contact with contactId " + contactId + " has been deleted successfully";
                ResponseDTO responseDTO = new ResponseDTO(messageForResponse);
                return MessageBuilder.withPayload(responseDTO).build();
            } else {
                String detailMessage = ContactsConstant.CONTACT_NOT_FOUND + contactId;
                throw new ContactNotFoundException("Post", "contactId", contactId, detailMessage);
            }
        }catch (ContactNotFoundException contactNotFoundException){
            log.error("The contact not found." + " Details : {} " ,contactNotFoundException.getMessage());
            MessageHeaders messageHeaders = ContactsUtil.createMessageHeaders(ContactsUtil.createHeaderMap(HttpStatus.NOT_FOUND));
            return MessageBuilder.createMessage(contactNotFoundException.getMessage(),messageHeaders);
        }
    }
}