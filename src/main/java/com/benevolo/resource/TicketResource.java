package com.benevolo.resource;

import com.benevolo.dto.BookingDTO;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void post(List<BookingDTO> bookings) {
        ticketService.save(bookings);
    }

    @PUT
    @Path("/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void put(@PathParam("ticketId") String ticketId, TicketDTO ticketDTO) {
        ticketService.update(ticketId, ticketDTO);
    }

}