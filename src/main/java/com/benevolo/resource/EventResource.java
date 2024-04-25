package com.benevolo.resource;

import com.benevolo.entity.Ticket;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/events/{eventId}")
public class EventResource {

    private final TicketService ticketService;

    @Inject
    public EventResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GET
    @Path("/tickets/{pageIndex}/{pageSize}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> get(@PathParam("eventId") String id, @PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize) {
        return ticketService.findByEventId(id, pageIndex, pageSize);
    }

}
