package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
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

@ApplicationScoped
public class MailService {

    @Inject
    Mailer mailer;

    @Inject
    RefundLinkService refundLinkService;

    @Inject
    Template emailTemplate;

    private static final String BENEVOLO_REFUND_URL = "https://shop.benevolo.de/refund/?";

    public void send(EmailBuilder emailBuilder) {
        TemplateInstance templateInstance = emailTemplate
                .data("headline", emailBuilder.getHeadline())
                .data("emailSubject", emailBuilder.getSubject())
                .data("content", emailBuilder.getContent())
                .data("refundLink", "");

        String renderedContent = templateInstance.render();

        mailer.send(
                Mail.withHtml(emailBuilder.getCustomerMail(), emailBuilder.getSubject(), renderedContent)
        );
    }

    public void sendEmailWithPdf(EmailBuilder emailBuilder, PDDocument pdf, Booking booking) throws IOException {
        try (ByteArrayOutputStream ticketOutputStream = new ByteArrayOutputStream()) {
            pdf.save(ticketOutputStream);
            Customer customer = booking.getCustomer();
            String refundLink = BENEVOLO_REFUND_URL + refundLinkService.findIdByBookingId(booking.getId());
            TemplateInstance templateInstance = emailTemplate
                    .data("headline", emailBuilder.getHeadline())
                    .data("emailSubject", emailBuilder.getSubject())
                    .data("content", emailBuilder.getContent())
                    .data("refundLink", refundLink);

            String renderedContent = templateInstance.render();

            mailer.send(
                    Mail.withHtml(customer.getEmail(), emailBuilder.getSubject(), renderedContent)
                            .addAttachment("ticket.pdf",
                                    ticketOutputStream.toByteArray(),
                                    "application/pdf")
            );
        }
    }
}
