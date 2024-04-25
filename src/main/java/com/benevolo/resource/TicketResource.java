package com.benevolo.resource;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Ticket;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/tickets")
public class TicketResource {

    private final TicketService ticketService;

    @Inject
    public TicketResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void post(Booking booking) {
        ticketService.save(booking);
    }

    @PUT
    @Path("/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void put(@PathParam("ticketId") String ticketId, Ticket ticket) {
        ticketService.update(ticketId, ticket);
    }

}