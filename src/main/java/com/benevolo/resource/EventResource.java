package com.benevolo.resource;

import com.benevolo.dto.TicketDTO;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    public Response get(@PathParam("eventId") String id, @PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize) {
        return Response.ok(ticketService.findByEventId(id, pageIndex, pageSize), MediaType.APPLICATION_JSON).header("X-Page-Size", ticketService.countByEventId(id, pageSize)).build();
    }

}
