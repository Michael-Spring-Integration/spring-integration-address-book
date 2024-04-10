package com.michael.spring.integration.address.book.service.implementation;

import com.michael.spring.integration.address.book.entity.Contact;
import com.michael.spring.integration.address.book.exception.ContactAlreadyExistsException;
import com.michael.spring.integration.address.book.exception.ContactNotFoundException;
import com.michael.spring.integration.address.book.exception.InvalidContactIdException;
import com.michael.spring.integration.address.book.mapper.ContactMapper;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import com.michael.spring.integration.address.book.model.response.ResponseDTO;
import com.michael.spring.integration.address.book.repository.ContactRepository;
import com.michael.spring.integration.address.book.service.ContactService;
import com.michael.spring.integration.address.book.util.ContactsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.michael.spring.integration.address.book.constants.ContactsConstant.*;

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
    @ServiceActivator(inputChannel = ADD_CONTACT_CHANNEL, outputChannel = HTTP_REPLY_CHANNEL)
    public Message createContact(ContactDTO contactDTO) {
        log.info(CONTACT_DETAILS_RECEIVED, contactDTO);
        Contact contactToBeCreated = ContactMapper.mapToContact(contactDTO);

        // Check for the existence
        if(contactDTO.getContactId()!=null &&
                contactRepository.findById(contactDTO.getContactId()).isPresent()){
            String detailMessage = new StringBuilder(CONTACT_ALREADY_EXISTING_FOR_GIVEN_ID).append(contactDTO.getContactId()).toString();
            throwContactAlreadyExistsException(detailMessage);
        } else if(contactDTO.getFullName()!=null) {
            List<Contact> contactList = contactRepository.findByFullName(contactDTO.getFullName());
            if(ObjectUtils.isNotEmpty(contactList)){
                String detailMessage = new StringBuilder(CONTACT_ALREADY_EXISTING_FOR_GIVEN_NAME).append(contactDTO.getFullName()).toString();
                throwContactAlreadyExistsException(detailMessage);
            }
        }
        // Create the new contact
        Contact contactCreated = contactRepository.save(contactToBeCreated);
        Map<String,Object> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.STATUS_CODE, HttpStatus.CREATED);
        return MessageBuilder.createMessage(contactCreated,ContactsUtil.createMessageHeaders(customHeaders));
    }

    @Override
    @ServiceActivator(inputChannel = UPDATE_CONTACT_CHANNEL, outputChannel = HTTP_REPLY_CHANNEL)
    public ContactDTO updateContact(ContactDTO contactDTO) {
        Long contactId = contactDTO.getContactId();
        Optional<Contact> contactDb = contactRepository.findById(contactId);
        if (contactDb.isPresent()) {
            Contact contactUpdate = contactDb.get();
            contactUpdate.setFullName(contactDTO.getFullName());
            contactUpdate.setEmailAddress(contactDTO.getEmailAddress());
            contactUpdate.setPhoneNumber(contactDTO.getPhoneNumber());
            contactRepository.save(contactUpdate);
            return ContactMapper.mapToContactDTO(contactUpdate);
        } else {
            String detailMessage = new StringBuilder(CONTACT_NOT_FOUND).append(contactId).toString();
            throw new ContactNotFoundException(RESOURCE_PUT, CONTACT_ID,detailMessage);
        }
    }

    @Override
    @ServiceActivator(inputChannel = SEARCH_ALL_CONTACTS_CHANNEL, outputChannel = HTTP_REPLY_CHANNEL)
    public List<ContactDTO> getAllContacts() {
        return ContactMapper.mapToContactDTOs(contactRepository.findAll());
    }

    @Override
    @ServiceActivator(inputChannel = GET_CONTACT_CHANNEL, outputChannel = HTTP_REPLY_CHANNEL)
    public ContactDTO getContactById(Message message) {
        if (message != null && message.getHeaders() != null) {
            log.info(HEADERS_OF_MESSAGE);
            message.getHeaders().forEach((key, value) -> log.info("{}: {}", key, value));
        }
        Long contactId = validateAndExtractContactId(message);
        log.info(CONTACT_ID_RECEIVED_IN_GET_CONTACT_BY_ID, contactId);
        Optional<Contact> contactDb = contactRepository.findById(contactId);
        if (contactDb.isPresent()) {
            return ContactMapper.mapToContactDTO(contactDb.get());
        }else {
            String detailMessage = new StringBuilder(CONTACT_NOT_FOUND).append(contactId).toString();
            throw new ContactNotFoundException(RESOURCE_GET, CONTACT_ID,detailMessage);
        }

    }

    @Override
    @ServiceActivator(inputChannel = SEARCH_CONTACTS_BY_NAME_CHANNEL , outputChannel = HTTP_REPLY_CHANNEL)
    public List<ContactDTO> searchContactsByName(@Payload String contactName,
                                                 @Header(OPERATION_NAME_IN_REQUEST_PARAMETER) String operationNameByRequestParam,
                                                 @Header(OPERATION_NAME_IN_CUSTOM_HEADER) String operationNameByRequestHeader) {
        log.info(CONTACT_NAME_RECEIVED_IN_SEARCH_CONTACTS_BY_NAME, contactName);
        log.info(CONTACT_NAME_RECEIVED_VIA_QUERY_PARAM, operationNameByRequestParam);
        log.info(CONTACT_NAME_RECEIVED_VIA_CUSTOM_HEADER, operationNameByRequestHeader);
        List<Contact> contactList = contactRepository.findByFullName(contactName);
        // Add the code if there is no search result found for the given search criteria
        if(ObjectUtils.isEmpty(contactList)){
            String detailMessage = CONTACT_NOT_FOUND_FOR_THE_GIVEN_NAME + contactName;
            throw new ContactNotFoundException(RESOURCE_GET, FULL_NAME,detailMessage);
        }
        return ContactMapper.mapToContactDTOs(contactList);
    }

    @Override
    @ServiceActivator(inputChannel = DELETE_CONTACT_CHANNEL, outputChannel = HTTP_REPLY_CHANNEL)
    public ResponseDTO deleteContact(Message message) {
        Long contactId = validateAndExtractContactId(message);
        log.info(CONTACT_ID_RECEIVED_IN_DELETE_CONTACT_BY_ID, contactId);
        Optional<Contact> contactDb = contactRepository.findById(contactId);
        if (contactDb.isPresent()) {
            contactRepository.delete(contactDb.get());
            String messageForResponse = String.format(CONTACT_DELETE_OPERATION_MSG,contactId);
            return new ResponseDTO(messageForResponse);
        } else {
            String detailMessage = new StringBuilder(CONTACT_NOT_FOUND).append(contactId).toString();
            throw new ContactNotFoundException(RESOURCE_DELETE, CONTACT_ID, detailMessage);
        }
    }

    private Long validateAndExtractContactId(Message message){
        try {
            if(message.getPayload() == null || (StringUtils.isBlank(message.getPayload().toString()))){
                log.error(CONTACT_ID_CANT_BE_NULL_BLANK);
                throw new InvalidContactIdException(CONTACT_ID , CONTACT_ID_CANT_BE_NULL_BLANK);
            }
            return Long.parseLong(message.getPayload().toString());
        }catch (NumberFormatException numberFormatException){
            log.error(CONTACT_ID_IS_INVALID + EXCEPTION_DETAILS , numberFormatException);
            throw new InvalidContactIdException(CONTACT_ID , CONTACT_ID_IS_INVALID);
        }
    }
    private void throwContactAlreadyExistsException(String detailMessage ){
        throw new ContactAlreadyExistsException(RESOURCE_POST,CONTACT_ID,detailMessage);
    }

}