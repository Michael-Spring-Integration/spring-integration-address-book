package com.michael.spring.integration.address.book.model.request;

import lombok.*;
import java.time.Instant;


@Data
public class ContactDTO {
    private Long contactId;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private Instant createdOn; // This field will have the type Timestamp in our generated schema
    private Instant lastUpdatedOn;
}
