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

@Path("/tickets/{eventId}")
public class TicketResource {

    private final TicketService ticketService;

    @Inject
    public TicketResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketDTO> get(@PathParam("eventId") String id) {
        return ticketService.findAllByEventId(id);
    }

}