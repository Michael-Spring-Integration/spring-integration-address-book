package com.michael.spring.integration.address.book.mapper;

import com.michael.spring.integration.address.book.entity.Contact;
import com.michael.spring.integration.address.book.model.request.ContactDTO;

import java.util.ArrayList;
import java.util.List;

public class ContactMapper {

    private ContactMapper(){

    }

    public static Contact mapToContact(ContactDTO contactDTO){
        Contact contact = new Contact();
        if(contactDTO.getContactId()!=null){
            contact.setContactId(contactDTO.getContactId());
        }
        contact.setFullName(contactDTO.getFullName());
        contact.setEmailAddress(contactDTO.getEmailAddress());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());
        return contact;
    }

    public static ContactDTO mapToContactDTO(Contact contact){
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setContactId(contact.getContactId());
        contactDTO.setFullName(contact.getFullName());
        contactDTO.setEmailAddress(contact.getEmailAddress());
        contactDTO.setPhoneNumber(contact.getPhoneNumber());
        contactDTO.setCreatedOn(contact.getCreatedOn());
        contactDTO.setLastUpdatedOn(contact.getLastUpdatedOn());
        return contactDTO;
    }

    public static List<ContactDTO> mapToContactDTOs(List<Contact> contactList){
        List<ContactDTO> contactDTOs = new ArrayList<>();
        contactList.forEach(contact -> contactDTOs.add(mapToContactDTO(contact)));
        return contactDTOs;
    }

}
