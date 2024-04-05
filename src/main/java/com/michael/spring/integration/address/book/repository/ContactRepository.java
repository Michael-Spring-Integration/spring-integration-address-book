package com.michael.spring.integration.address.book.repository;

import com.michael.spring.integration.address.book.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Long> {

    // It works if it matches the customer's fullName
    List<Contact> findByFullName(String fullName);
}
