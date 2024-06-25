package com.benevolo.rest;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Ticket;
import com.benevolo.rest.params.BookingSearchParams;
import com.benevolo.service.TicketService;
import com.benevolo.utils.TicketStatus;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/tickets")
public class TicketResource {

    @Inject
    TicketService ticketService;

    @RestClient
    TicketTypeClient ticketTypeClient;

    @PUT
    @Path("/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void put(@PathParam("ticketId") String ticketId, Ticket ticket) {
        ticketService.update(ticketId, ticket);
    }

    @PUT
    @Path("/{ticketId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateStatusByTicketId(@PathParam("ticketId") String ticketId) {
        ticketService.redeemTicket(ticketId);
    }

    @PUT
    @Path("/{ticketId}/status/cancelled")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void cancelTicket(@PathParam("ticketId") String ticketId) {
        ticketService.cancelTicket(ticketId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByBookingItemId(@QueryParam("bookingItemId") String bookingItemId) {
        return ticketService.findByBookingItemId(bookingItemId);
    }

    @GET
    @Path("/search/{pageIndex}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findBySearch(@PathParam("pageIndex") int pageIndex, @QueryParam("eventId") String eventId, @BeanParam BookingSearchParams params, @Context HttpServerResponse httpServerResponse) {
        final int PAGE_SIZE = 15;

        List<Ticket> result = ticketService.findBySearch(eventId, pageIndex, PAGE_SIZE, params);
        result.forEach(item -> {
            item.getBookingItem().setTicketType(ticketTypeClient.findById(item.getBookingItem().getTicketTypeId()));
            item.setCustomer(item.getBookingItem().getBooking().getCustomer());
            item.setBookedAt(item.getBookingItem().getBooking().getBookedAt());
        });

        httpServerResponse.headers().add("X-Page-Size", String.valueOf(ticketService.countByEventIdAndSearch(eventId, PAGE_SIZE, params)));

        return result;
    }

    @GET
    @Path("/public/{refundId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByBookingId(@PathParam("refundId") String refundId) {
        List<Ticket> tickets = ticketService.findByRefundId(refundId);
        tickets.forEach(ticket -> {
            ticket.getBookingItem().setTicketType(ticketTypeClient.findById(ticket.getBookingItem().getTicketTypeId()));
        });
        return tickets.stream().filter(item -> item.getStatus() == TicketStatus.VALID).toList();
    }

}