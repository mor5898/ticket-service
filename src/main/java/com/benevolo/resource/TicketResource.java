package com.benevolo.resource;

import com.benevolo.DTO.StatsDTO;
import com.benevolo.entity.Booking;
import com.benevolo.entity.Ticket;
import com.benevolo.repo.TicketRepo;
import com.benevolo.service.TicketService;
import com.benevolo.utils.TicketStatus;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@Path("")
public class TicketResource {

    private final TicketService ticketService;
    private final HttpServerResponse httpServerResponse;
    private final TicketRepo ticketRepo;

    @Inject
    public TicketResource(TicketService ticketService, HttpServerResponse httpServerResponse, TicketRepo ticketRepo) {
        this.ticketService = ticketService;
        this.httpServerResponse = httpServerResponse;
        this.ticketRepo = ticketRepo;
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
    @Path("/events/{eventId}/ticketstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatsDTO> getTicketStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        return ticketService.getTicketStatsByDay(eventId, startDate, endDate);
    }

    @GET
    @Path("/events/{eventId}/bookingstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatsDTO> getBookingStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        return ticketService.getBookingStatsByDay(eventId, startDate, endDate);
    }

    @GET
    @Path("/event/{eventId}/redeemed/amount")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(REQUIRES_NEW)
    public long getValidatedTicketsAmount(@PathParam("eventId") String eventId) {
        return ticketRepo.countByStatus(eventId, TicketStatus.REDEEMED);
    }

    @GET
    @Path("/event/{eventId}/valid-tickets/amount")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(REQUIRES_NEW)
    public long getUnvalidatedTicketsAmount(@PathParam("eventId") String eventId) {
        return ticketRepo.countByStatus(eventId, TicketStatus.VALID);
    }

}