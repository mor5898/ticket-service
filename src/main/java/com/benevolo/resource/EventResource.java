package com.benevolo.resource;

import com.benevolo.dto.TicketDTO;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/events/{eventId}")
public class EventResource {

    private final TicketService ticketService;

    @Inject
    public EventResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GET
    @Path("/tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketDTO> get(@PathParam("eventId") String id) {
        return ticketService.findAllByEventId(id);
    }

}
