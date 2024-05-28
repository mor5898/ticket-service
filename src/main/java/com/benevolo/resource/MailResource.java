package com.benevolo.resource;

import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jboss.logmanager.Level;

import java.io.IOException;
import java.util.logging.Logger;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON) // not sure about this?
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {

    private static final Logger LOGGER = Logger.getLogger(MailResource.class.getName());

    private final PdfService pdfService;

    private final MailService mailService;

    @Inject
    public MailResource(PdfService pdfService, MailService mailService) {
        this.pdfService = pdfService;
        this.mailService = mailService;
    }

    @GET
    @Path("/{bookingId}")
    public void buildAndSendMail(@PathParam("bookingId") String bookingId) throws WebApplicationException {
        PDDocument pdf = null;
        try {
            pdf = pdfService.createPdf(bookingId);
            mailService.sendEmail(pdf, bookingId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler", e);
            throw new WebApplicationException("Error while building and sending mail", Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            if (pdf != null) {
                try {
                    pdf.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error closing PDF document");
                }
            }
        }
    }
}