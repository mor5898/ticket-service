package com.benevolo.resource;

import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import com.benevolo.service.QrCodeService;
import io.vertx.core.http.HttpServerResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON) // not sure about this?
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {

    private final HttpServerResponse httpServerResponse;

    private final PdfService pdfService;

    private final QrCodeService qrCodeService;

    private final MailService mailService;

    @Inject
    public MailResource(HttpServerResponse httpServerResponse, PdfService pdfService, QrCodeService qrCodeService, MailService mailService) {
        this.httpServerResponse = httpServerResponse;
        this.pdfService = pdfService;
        this.qrCodeService = qrCodeService;
        this.mailService = mailService;
    }

    @GET
    @Path("/{ticketId}")
    public void buildAndSendMail(@PathParam("ticketId") String ticketId) {
        try {
            byte[] qrCode = qrCodeService.generateQRCode(ticketId);
            if (qrCode == null) {
                httpServerResponse.setStatusCode(404).end("No Ticket found for id: " + ticketId);
                return;
            }
            PDDocument pdf = pdfService.createPdf(qrCode);
            mailService.sendEmail(pdf);
            httpServerResponse.setStatusCode(201).end("Ticket successfully send.");
        } catch (Exception e) {
            httpServerResponse.setStatusCode(500).end("Server Error: " + e);
        }
    }
}

