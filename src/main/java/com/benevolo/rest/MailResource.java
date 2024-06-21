package com.benevolo.rest;

import com.benevolo.entity.Booking;
import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import com.benevolo.utils.EmailBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jboss.logmanager.Level;

import java.awt.print.Book;
import java.util.logging.Logger;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {

    private static final Logger LOGGER = Logger.getLogger(MailResource.class.getName());

    @Inject
    PdfService pdfService;

    @Inject
    MailService mailService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void send(EmailBuilder emailBuilder) {
        try {
            mailService.send(emailBuilder);
        } catch (Exception e) {
            String msg = "Error while sending cancellation approval";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new WebApplicationException(msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/attachment")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendEmailWithPdf(EmailBuilder emailBuilder) {
        Booking booking = Booking.findById(emailBuilder.getBookingId());
        try (PDDocument pdf = pdfService.createPdf(booking.getId())) {
            mailService.sendEmailWithPdf(emailBuilder, pdf, booking);
        } catch (Exception e) {
            String msg = "Error while building and sending mail";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new WebApplicationException(msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }






    // this is all supposed to be removed
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

    @POST
    @Path("/cancellation/approval/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendCancellationApproval(@PathParam("ticketId") String ticketId) throws WebApplicationException {
        try {
            mailService.sendCancellation(ticketId, true);
        } catch (Exception e) {
            String msg = "Error while sending cancellation approval";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new WebApplicationException(msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/cancellation/rejection/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendCancellationRejection(@PathParam("ticketId") String ticketId) throws WebApplicationException {
        try {
            mailService.sendCancellation(ticketId, false);
        } catch (Exception e) {
            String msg = "Error while sending cancellation rejection";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new WebApplicationException(msg, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}