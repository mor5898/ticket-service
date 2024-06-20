package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import com.benevolo.entity.Ticket;
import com.benevolo.utils.EmailBuilder;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class MailService {

    @Inject
    Mailer mailer;

    @Inject
    RefundLinkService refundLinkService;

    @Inject
    Template emailTemplate;

    private static final String BENEVOLO_REFUND_URL = "https://shop.benevolo.de/refund/?";

    public void sendEmailWithPdf(PDDocument pdf, String bookingId) throws IOException {
        try (ByteArrayOutputStream ticketOutputStream = new ByteArrayOutputStream()) {
            pdf.save(ticketOutputStream);
            Booking booking = Booking.findById(bookingId);
            Customer customer = booking.getCustomer();

            mailer.send(
                    Mail.withText(customer.getEmail(),
                            "Tickets Benevolo Shop",
                            "Ihre Tickets sind im Anhang zu finden. \n \n" +
                                    "Sie können die Tickets zu ihrer Bestellung unter folgendem Link stornieren: \n" +
                                    BENEVOLO_REFUND_URL + refundLinkService.findIdByBookingId(bookingId)
                    ).addAttachment("ticket.pdf",
                            ticketOutputStream.toByteArray(),
                            "application/pdf")
            );
        }
    }

    public void sendCancellation(String ticketId, boolean isApproved) {
        Ticket ticket = Ticket.findById(ticketId);
        Customer customer = ticket.getBookingItem().getBooking().getCustomer();
        String emailText = "Die Stornierung ihrer Tickets konnte " + (isApproved ? "erfolgreich" : "nicht erfolgreich") +
                " durchgeführt werden. Bei weiteren Fragen, wenden sie sich bitte an den Benevolo-Support.";
        mailer.send(
                Mail.withText(customer.getEmail(),
                        "Stornierung Ticket Benevolo Shop",
                        emailText
                )
        );
    }

    public void send(EmailBuilder emailBuilder) {
        TemplateInstance templateInstance = emailTemplate
                .data("headline", emailBuilder.getHeadline())
                .data("emailSubject", emailBuilder.getSubject())
                .data("content", emailBuilder.getContent());

        String renderedContent = templateInstance.render();

        mailer.send(
                Mail.withHtml(emailBuilder.getRecipientEmail(), emailBuilder.getSubject(), renderedContent)
        );
    }
}
