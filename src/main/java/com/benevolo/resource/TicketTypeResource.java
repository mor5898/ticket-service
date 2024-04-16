package com.benevolo.resource;

import com.benevolo.dto.TicketDTO;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/ticket-type/{ticketTypeId}")
public class TicketTypeResource {

    private final TicketService ticketService;

    @Inject
    public TicketTypeResource(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @POST
    @Path("/ticket")
    public void post(@PathParam("ticketTypeId") String ticketTypeId) {
        ticketService.save(ticketTypeId);
    }

}
