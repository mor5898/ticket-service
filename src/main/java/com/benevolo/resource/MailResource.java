package com.benevolo.resource;

import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
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

    private final MailService mailService;

    @Inject
    public MailResource(HttpServerResponse httpServerResponse, PdfService pdfService, MailService mailService) {
        this.httpServerResponse = httpServerResponse;
        this.pdfService = pdfService;
        this.mailService = mailService;
    }

    @GET
    @Path("/{eventName}/{eventId}/{bookingId}")
    public void buildAndSendMail(@PathParam("eventName") String eventName, @PathParam("eventId") String eventId ,@PathParam("bookingId") String bookingId) {
        try {
            PDDocument pdf = pdfService.createPdf(eventName, eventId, bookingId);
            mailService.sendEmail(pdf);
            httpServerResponse.setStatusCode(201).end("Ticket successfully send.");
        } catch (Exception e) {
            httpServerResponse.setStatusCode(500).end("Server Error: " + e);
        }
    }
}

