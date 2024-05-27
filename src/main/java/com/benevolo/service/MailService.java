package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ApplicationScoped
public class MailService {

    private final Mailer mailer;

    @Inject
    public MailService(Mailer mailer) {
        this.mailer = mailer;
    }

    public void sendEmail(PDDocument pdf, String bookingId) throws IOException {
        ByteArrayOutputStream ticketOutputStream = new ByteArrayOutputStream();
        pdf.save(ticketOutputStream);
        Booking booking = Booking.findById(bookingId);
        Customer customer = booking.getCustomer();

        mailer.send(
                Mail.withText(customer.getEmail(),
                        "Tickets Benevolo Shop",
                        "Ihre Tickets sind im Anhang zu finden. :)"
                ).addAttachment("ticket.pdf",
                        ticketOutputStream.toByteArray(),
                        "application/pdf")
        );
    }
}
