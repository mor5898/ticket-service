package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.dto.StatsDTO;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Ticket;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class TicketService {

    @RestClient
    TicketTypeClient ticketTypeClient;

    @Inject
    TicketRepo ticketRepo;

    @Inject
    BookingRepo bookingRepo;

    @Transactional
    public void update(String ticketId, Ticket ticket) {
        Ticket ticketEntity = ticketRepo.findById(ticketId);
        ticketEntity.setStatus(ticket.getStatus());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setTaxRate(ticket.getTaxRate());
        ticketRepo.persist(ticketEntity);
    }

    @Transactional
    public void redeemTicket(String ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId);
        if (ticket.getStatus() == TicketStatus.VALID) {
            ticket.setStatus(TicketStatus.REDEEMED);
            ticketRepo.persist(ticket);
            return;
        }
        throw new BadRequestException(Response.ok("invalid_ticket_status").status(400).build());
    }

    public Ticket generateTicket(BookingItem bookingItem) {
        TicketType ticketType = ticketTypeClient.findById(bookingItem.getTicketTypeId());
        return new Ticket(TicketStatus.VALID, ticketType.getPrice(), ticketType.getTaxRate());
    }

    public List<StatsDTO> getTicketStatsByDay(String eventId, String startDate, String endDate) {
        LocalDate end = LocalDate.parse(endDate).plusDays(1);

        List<LocalDate> dates = LocalDate.parse(startDate).datesUntil(end).toList();
        List<StatsDTO> statsByDate = new LinkedList<>();

        for (LocalDate date : dates) {
            long countTickets = ticketRepo.countByDate(eventId, date);
            statsByDate.add(new StatsDTO(date.toString(), countTickets));
        }
        return statsByDate;
    }

    public List<StatsDTO> getBookingStatsByDay(String eventId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate).plusDays(1);

        List<LocalDate> dates = start.datesUntil(end).toList();
        List<StatsDTO> statsByDate = new LinkedList<>();

        for (LocalDate date : dates) {
            long countBookings = bookingRepo.countByDate(eventId, date);
            statsByDate.add(new StatsDTO(date.toString(), countBookings));
        }
        return statsByDate;
    }

    public List<Ticket> findByBookingItemId(String bookingItemId) {
        return ticketRepo.findByBookingItemId(bookingItemId);
    }
}