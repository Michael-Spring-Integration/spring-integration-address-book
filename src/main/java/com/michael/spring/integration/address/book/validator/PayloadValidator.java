package com.michael.spring.integration.address.book.validator;

import com.michael.spring.integration.address.book.exception.InvalidContactDetailsException;
import com.michael.spring.integration.address.book.model.request.ContactDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.michael.spring.integration.address.book.constants.ContactsConstant.EMAIL_REGULAR_EXPRESSION_FOR_VALIDATION;
import static com.michael.spring.integration.address.book.constants.ContactsConstant.PHONE_REGULAR_EXPRESSION_FOR_VALIDATION;

public class PayloadValidator implements Validator {

    public static final Pattern EMAIL_VALIDATION_PATTERN;
    public static final Pattern PHONE_VALIDATION_PATTERN;

    static {
        EMAIL_VALIDATION_PATTERN = Pattern.compile(EMAIL_REGULAR_EXPRESSION_FOR_VALIDATION);
        PHONE_VALIDATION_PATTERN = Pattern.compile(PHONE_REGULAR_EXPRESSION_FOR_VALIDATION);
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return ContactDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContactDTO  contactDTO = (ContactDTO) target;
        String emailAddress = contactDTO.getEmailAddress();
        String phoneNumber = contactDTO.getPhoneNumber();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "contactDTO.fullName.empty", "Full name must not be empty");

        if (emailAddress == null || StringUtils.isBlank(emailAddress)
                || !patternMatches(emailAddress, EMAIL_VALIDATION_PATTERN)) {
            errors.reject("contactDTO.emailAddress.invalid", "Invalid email address");
        }

        if(phoneNumber==null || StringUtils.isBlank(phoneNumber)
                || !patternMatches(phoneNumber, PHONE_VALIDATION_PATTERN)){
            errors.reject("contactDTO.phoneNumber.invalid", "Invalid phone number");
        }

        if (errors.hasErrors()) {
            throw new InvalidContactDetailsException(errors.getAllErrors());
        }
    }
    public static boolean patternMatches(String fieldValue, Pattern pattern) {
        return pattern.matcher(fieldValue).matches();
    }
}