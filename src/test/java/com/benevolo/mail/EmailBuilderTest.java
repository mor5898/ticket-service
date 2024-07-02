package com.benevolo.mail;

import com.benevolo.utils.EmailBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailBuilderTest {

    @Test
    void testGetHeadline() {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setHeadline("Test Headline");

        assertEquals("Test Headline", emailBuilder.getHeadline());
    }

    @Test
    void testGetSubject() {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setSubject("Test Subject");

        assertEquals("Test Subject", emailBuilder.getSubject());
    }

    @Test
    void testGetContent() {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setContent("Test Content");

        assertEquals("Test Content", emailBuilder.getContent());
    }

    @Test
    void testGetCustomerMail() {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setCustomerMail("customer@example.com");

        assertEquals("customer@example.com", emailBuilder.getCustomerMail());
    }
}
