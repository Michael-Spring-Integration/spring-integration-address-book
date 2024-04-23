package com.michael.spring.integration.address.book.validator;

import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

public class PayloadValidatorTest {

    @Test
    public void testSupportingClassType(){
        PayloadValidator payloadValidator = new PayloadValidator();
        boolean result = payloadValidator.supports(ContactDTO.class);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }
    @Test
    public void testValidatePayload(){
        String fullName = "";
        String emailAddress = "michaelraj.p@gmail.com.";
        String phoneNumber = "+91652";

        ContactDTO c1 = new ContactDTO();
        c1.setFullName(fullName);
        c1.setEmailAddress(emailAddress);
        c1.setPhoneNumber(phoneNumber);

        String objectName = "requestPayload";

        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(c1,objectName);
        PayloadValidator payloadValidator = new PayloadValidator();
        InvalidContactDetailsException invalidContactDetailsException = Assertions.assertThrows(InvalidContactDetailsException.class,()->{
            payloadValidator.validate(c1,beanPropertyBindingResult);
        },"Expected InvalidContactDetailsException to be thrown");

        Assertions.assertNotNull(invalidContactDetailsException);
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors());
        Assertions.assertEquals(3,invalidContactDetailsException.getObjectErrors().size());
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(0));
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(0).getDefaultMessage());
        Assertions.assertEquals("Full name must not be empty",invalidContactDetailsException.getObjectErrors().get(0).getDefaultMessage());

        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(1));
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(1).getDefaultMessage());
        Assertions.assertEquals("Invalid email address",invalidContactDetailsException.getObjectErrors().get(1).getDefaultMessage());

        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(2));
        Assertions.assertNotNull(invalidContactDetailsException.getObjectErrors().get(2).getDefaultMessage());
        Assertions.assertEquals("Invalid phone number",invalidContactDetailsException.getObjectErrors().get(2).getDefaultMessage());

    }
}
