package com.michael.spring.integration.address.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table (name = "Contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contactId;

    @Column(name = "name")
    private String fullName;

    @Column(name = "emailAddress")
    private String emailAddress;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @CreationTimestamp
    private Instant createdOn; // This field will have the type Timestamp in our generated schema

    @UpdateTimestamp
    private Instant lastUpdatedOn; // This field will have the type Timestamp in our generated schema


}
