package com.michael.spring.integration.address.book.constants;

public class ContactsConstant {

    private ContactsConstant(){

    }

    public static final String CONTACT_ALREADY_EXISTING_FOR_GIVEN_ID = "The given contact is already existing for the given Contact ID : ";

    public static final String CONTACT_ALREADY_EXISTING_FOR_GIVEN_NAME = "The given contact is already existing for the given name : ";

    public static final String CONTACT_NOT_FOUND = "The contact is not found for the given Contact ID : ";

    public static final String CONTACT_NOT_FOUND_FOR_THE_GIVEN_NAME = "The contact is not found for the given name : ";

    public static final String CONTACT_ID_CANT_BE_NULL_BLANK = "ContactId can't be null or blank";

    public static final String CONTACT_ID_IS_INVALID = "ContactId is invalid";

    public static final String CONTACT_ID = "contactId";

    public static final String FULL_NAME = "fullName";


    public static final String RESOURCE_POST = "POST";

    public static final String RESOURCE_PUT = "PUT";

    public static final String RESOURCE_GET = "GET";

    public static final String RESOURCE_DELETE = "DELETE";

    public static final String HTTP_REPLY_CHANNEL = "httpReplyChannel";

    public static final String ADD_CONTACT_CHANNEL = "addContactChannel";

    public static final String UPDATE_CONTACT_CHANNEL = "updateContactChannel";

    public static final String SEARCH_ALL_CONTACTS_CHANNEL = "searchAllContactsChannel";

    public static final String GET_CONTACT_CHANNEL = "getContactChannel";

    public static final String SEARCH_CONTACTS_BY_NAME_CHANNEL = "searchContactsByNameChannel";

    public static final String DELETE_CONTACT_CHANNEL = "deleteContactChannel";

    public static final String OPERATION_NAME_IN_REQUEST_PARAMETER = "operationNameInRequestParameter";

    public static final String OPERATION_NAME_IN_CUSTOM_HEADER = "operationNameInCustomHeader";

    public static final String HEADERS_OF_MESSAGE = "The values of all http headers from the message : \n ";

    public static final String CONTACT_DETAILS_RECEIVED = "The contact details received : {} ";

    public static final String CONTACT_ID_RECEIVED_IN_GET_CONTACT_BY_ID ="The contactId received in getContactById() : {} ";
    public static final String CONTACT_ID_RECEIVED_IN_DELETE_CONTACT_BY_ID ="The contactId received in deleteContact() : {} ";

    public static final String EXCEPTION_DETAILS = "The exception details are : {} ";

    public static final String CONTACT_DELETE_OPERATION_MSG = "Contact with contactId %s has been deleted successfully";

    public static final String CONTACT_NAME_RECEIVED_IN_SEARCH_CONTACTS_BY_NAME = "The contactName received in searchContactsByName() : {} ";

    public static final String CONTACT_NAME_RECEIVED_VIA_QUERY_PARAM = "The operationName received in searchContactsByName() via Request or Query Parameter : {} ";

    public static final String CONTACT_NAME_RECEIVED_VIA_CUSTOM_HEADER = "The operationName received in searchContactsByName() via Request Custom Request Header : {} ";
}
