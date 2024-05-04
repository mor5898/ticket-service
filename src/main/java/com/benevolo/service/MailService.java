package com.benevolo.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;

public class MailService {

    private final Mailer mailer;

    @Inject
    public MailService(Mailer mailer) {
        this.mailer = mailer;
    }

    @Blocking // not sure about this?
    public void sendEmail(PDDocument pdf) {
        // to do
        mailer.send(
                Mail.withText("test@test.com",
                        "Ahoy from Quarkus",
                        "A simple email sent from a Quarkus application."
                )
        );
    }
}
