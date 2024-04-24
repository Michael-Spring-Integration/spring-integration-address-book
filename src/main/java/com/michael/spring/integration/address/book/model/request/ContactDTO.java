package com.michael.spring.integration.address.book.model.request;

import lombok.*;
import java.time.Instant;


@Data
public class ContactDTO {
    private Long contactId;

   // @NotBlank(message = "Full name is required")
    private String fullName;

   // @Email(message = "Invalid email address")
    private String emailAddress;

   // @Pattern(regexp = "\\+\\d{1,3}\\d{10}", message = "Phone number must be in the format +[country code][10 digit number]")
    private String phoneNumber;

    private Instant createdOn; // This field will have the type Timestamp in our generated schema

    private Instant lastUpdatedOn;
}
