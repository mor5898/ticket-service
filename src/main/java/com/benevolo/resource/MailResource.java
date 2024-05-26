package com.benevolo.resource;

import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON) // not sure about this?
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {

    private final PdfService pdfService;

    private final MailService mailService;

    @Inject
    public MailResource(PdfService pdfService, MailService mailService) {
        this.pdfService = pdfService;
        this.mailService = mailService;
    }

    @GET
    @Path("/{eventId}/{bookingId}")
    public void buildAndSendMail(@PathParam("eventId") String eventId ,@PathParam("bookingId") String bookingId) throws WebApplicationException {
        try {
            PDDocument pdf = pdfService.createPdf(eventId, bookingId);
            mailService.sendEmail(pdf);
        } catch (Exception e) {
            throw new WebApplicationException("Error while building and sending mail", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}