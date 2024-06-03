package com.benevolo.rest;

import com.benevolo.client.ProcessEngineClient;
import com.benevolo.dto.TicketRedeemDTO;
import com.benevolo.entity.Ticket;
import com.benevolo.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/tickets")
public class TicketResource {

    @Inject
    TicketService ticketService;

    @RestClient
    ProcessEngineClient processEngineClient;

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
    @Path("/ticket/{ticketId}/redeem")
    @Produces(MediaType.APPLICATION_JSON)
    public void startRedeemTicketProcess(@PathParam("ticketId") String ticketId) {
        processEngineClient.startTicketRedeemProcess(new TicketRedeemDTO(ticketId, "eventId"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByBookingItemId(@QueryParam("bookingItemId") String bookingItemId) {
        return ticketService.findByBookingItemId(bookingItemId);
    }


}