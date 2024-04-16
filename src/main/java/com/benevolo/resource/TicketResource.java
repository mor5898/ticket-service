package com.benevolo.resource;

import com.benevolo.dto.TicketDTO;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/tickets")
public class TicketResource {

    private final TicketService ticketService;

    @Inject
    public TicketResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PUT
    @Path("/{ticketId}")
    public void put(@PathParam("ticketId") String ticketId, TicketDTO ticketDTO) {
        ticketService.update(ticketId, ticketDTO);
    }

}