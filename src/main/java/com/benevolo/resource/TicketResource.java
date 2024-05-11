package com.benevolo.resource;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Ticket;
import com.benevolo.service.TicketService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("")
public class TicketResource {

    private final TicketService ticketService;
    private final HttpServerResponse httpServerResponse;

    @Inject
    public TicketResource(TicketService ticketService, HttpServerResponse httpServerResponse) {
        this.ticketService = ticketService;
        this.httpServerResponse = httpServerResponse;
    }

    @POST
    @Path("/tickets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void post(Booking booking) {
        ticketService.save(booking);
    }

    @PUT
    @Path("/tickets/{ticketId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void put(@PathParam("ticketId") String ticketId, Ticket ticket) {
        ticketService.update(ticketId, ticket);
    }

    @PATCH
    @Path("/tickets/{ticketId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateStatusByTicketId(@PathParam("ticketId") String ticketId) {
        ticketService.redeemTicket(ticketId);
    }

    @GET
    @Path("/events/{eventId}/tickets/{pageIndex}/{pageSize}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> get(@PathParam("eventId") String id, @PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize) {
        httpServerResponse.headers().add("X-Page-Size", String.valueOf(ticketService.countByEventId(id, pageSize)));
        return ticketService.findByEventId(id, pageIndex, pageSize);
    }

    @GET
    @Path("/tickets/{eventId}/ticketstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ObjectNode> getTicketStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate){
        return ticketService.getTicketStatsByDay(eventId, startDate, endDate);
    }

    @GET
    @Path("/tickets/{eventId}/bookingstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ObjectNode> getBookingStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate){
        return ticketService.getBookingStatsByDay(eventId, startDate, endDate);
    }

}