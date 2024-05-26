package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import com.benevolo.repo.TicketRepo;
import com.benevolo.entity.Ticket;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ApplicationScoped
public class MailService {

    private final Mailer mailer;


    @Inject
    public MailService(Mailer mailer, TicketRepo ticketRepo) {
        this.mailer = mailer;
    }

   Booking booking = new Booking();
    Customer customer = new Customer();

    public void sendEmail(PDDocument pdf) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdf.save(outputStream);
        pdf.close();

        byte[] pdfData = outputStream.toByteArray();

        mailer.send(
                Mail.withText("mor5898@thi.de",
                        "Tickets Benevolo Shop",
                        "Ihre Tickets sind im Anhang zu finden. :)"
                ).addAttachment("ticket.pdf",
                        pdfData,
                        "application/pdf")
        );
    }
}
