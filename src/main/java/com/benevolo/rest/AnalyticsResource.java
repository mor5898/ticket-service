package com.benevolo.rest;

import com.benevolo.dto.StatsDTO;
import com.benevolo.repo.TicketRepo;
import com.benevolo.service.TicketService;
import com.benevolo.utils.TicketStatus;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@Path("/events/{eventId}")
public class AnalyticsResource {

    @Inject
    TicketService ticketService;

    @Inject
    TicketRepo ticketRepo;

    @GET
    @Path("/ticketstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatsDTO> getTicketStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        return ticketService.getTicketStatsByDay(eventId, startDate, endDate);
    }

    @GET
    @Path("/bookingstatsbyday/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatsDTO> getBookingStatsByDay(@PathParam("eventId") String eventId, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        return ticketService.getBookingStatsByDay(eventId, startDate, endDate);
    }

    @GET
    @Path("/redeemed/amount")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(REQUIRES_NEW)
    public long getValidatedTicketsAmount(@PathParam("eventId") String eventId) {
        return ticketRepo.countByStatus(eventId, TicketStatus.REDEEMED);
    }

    @GET
    @Path("/valid-tickets/amount")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(REQUIRES_NEW)
    public long getUnvalidatedTicketsAmount(@PathParam("eventId") String eventId) {
        return ticketRepo.countByStatus(eventId, TicketStatus.VALID);
    }

}
