package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import com.benevolo.repo.TicketRepo;
import com.benevolo.entity.Ticket;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@ApplicationScoped
public class MailService {

    private final Mailer mailer;


    @Inject
    public MailService(Mailer mailer, TicketRepo ticketRepo) {
        this.mailer = mailer;
    }

   Booking booking = new Booking();
    Customer customer = new Customer();

    public void sendEmail(PDDocument pdf) {
        ByteArrayOutputStream ticketOuputStream = new ByteArrayOutputStream();

        mailer.send(
                Mail.withText("tow6220@thi.de",
                        "Hier könnte ihr Betreff stehen",
                        "Hier sollte ihr Ticket sein :)"
                ).addAttachment("Ticket.pdf",
                        ticketOuputStream.toByteArray(),
                        "application/pdf")
        );
    }
}
