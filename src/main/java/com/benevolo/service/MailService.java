package com.benevolo.service;

import com.benevolo.entity.Customer;
import com.benevolo.repo.TicketRepo;
import com.benevolo.entity.Ticket;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;

@ApplicationScoped
public class MailService {

    private final Mailer mailer;


    @Inject
    public MailService(Mailer mailer, TicketRepo ticketRepo) {
        this.mailer = mailer;
    }

    Ticket ticket = new Ticket();
    Customer customer = new Customer();

    public void sendEmail(PDDocument pdf) {

        mailer.send(
                Mail.withText(customer.getEmail(),
                        "#Ticketid:"+ticket.getId(),
                        "A simple email sent from a Quarkus application."
                ).addAttachment(ticket.getId(),
                        "contentAttachment".getBytes(),
                        "application/pdf")
        );
    }
}
