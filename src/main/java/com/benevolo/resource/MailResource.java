package com.benevolo.resource;

import com.benevolo.entity.Booking;
import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jboss.logmanager.Level;
import java.util.logging.Logger;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON) // not sure about this?
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {

    private static final Logger LOGGER = Logger.getLogger(MailResource.class.getName());

    @Inject
    PdfService pdfService;

    @Inject
    MailService mailService;

    @POST
    @Path("/tickets")
    @Consumes(MediaType.APPLICATION_JSON)
    public void buildAndSendMail(Booking booking) throws WebApplicationException {
        try (PDDocument pdf = pdfService.createPdf(booking.getId())) {
            mailService.sendEmailWithPdf(pdf, booking.getId());
        } catch (Exception e) {
            String msg = "Error while building and sending mail";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new WebApplicationException(msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}