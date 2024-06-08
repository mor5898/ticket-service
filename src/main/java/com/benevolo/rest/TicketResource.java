package com.benevolo.rest;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Ticket;
import com.benevolo.rest.params.BookingSearchParams;
import com.benevolo.service.TicketService;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByBookingItemId(@QueryParam("bookingItemId") String bookingItemId) {
        return ticketService.findByBookingItemId(bookingItemId);
    }

    @GET
    @Path("/search/{pageIndex}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findBySearch(@PathParam("pageIndex") int pageIndex, @QueryParam("eventId") String eventId, @BeanParam BookingSearchParams params, @Context HttpServerResponse httpServerResponse) {
        List<Ticket> result = ticketService.findBySearch(eventId, pageIndex, 15, params);
        result.forEach(item -> {
            item.getBookingItem().setTicketType(ticketTypeClient.findById(item.getBookingItem().getTicketTypeId()));
            item.setCustomer(item.getBookingItem().getBooking().getCustomer());
            item.setBookedAt(item.getBookingItem().getBooking().getBookedAt());
        });

        httpServerResponse.headers().add("X-Page-Size", String.valueOf(69));

        return result;
    }

    @GET
    @Path("/public/{refundId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByBookingId(@PathParam("refundId") String refundId) {
        return ticketService.findByRefundId(refundId);
    }

}