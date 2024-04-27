package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.*;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class TicketService {

    @RestClient
    TicketTypeClient ticketTypeClient;

    private final TicketRepo ticketRepo;

    @Inject
    public TicketService(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public List<Ticket> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        List<Ticket> tickets = ticketRepo.findByEventId(eventId, pageIndex, pageSize);
        tickets.forEach(item -> {
            BookingItem bookingItem = item.getBookingItem();
            bookingItem.setTicketType(ticketTypeClient.findById(bookingItem.getTicketTypeId()));
        });
        return tickets;
    }

    public long countByEventId(String eventId, Integer pageSize) {
        return (long) Math.ceil((ticketRepo.countByEventId(eventId) * 1.0) / pageSize);
    }

    public void update(String ticketId, Ticket ticket) {
        Ticket ticketEntity = ticketRepo.findById(ticketId);
        ticketEntity.setStatus(ticket.getStatus());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setTaxRate(ticket.getTaxRate());
        ticketRepo.persist(ticketEntity);
    }

    public void redeemTicket(String ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId);
        if(ticket.getStatus() == TicketStatus.VALID) {
            ticket.setStatus(TicketStatus.REDEEMED);
            ticketRepo.persist(ticket);
            return;
        }
        throw new BadRequestException(Response.ok("invalid_ticket_status").status(400).build());
    }


    @Transactional
    public void save(Booking booking) {
        for(BookingItem bookingItem : booking.getBookingItems()) {
            bookingItem.setBooking(booking);
            booking.setBookedAt(LocalDateTime.now());
            bookingItem.setTicketType(ticketTypeClient.findById(bookingItem.getTicketTypeId()));
            bookingItem.setTickets(new LinkedList<>());
            for(int i = 0; i < bookingItem.getQuantity(); i++) {
                bookingItem.addTicket(generateTicket(bookingItem));
            }
        }
        Booking.persist(booking);
        List<Ticket> t = Ticket.listAll();
    }

    private Ticket generateTicket(BookingItem bookingItem) {
        TicketType ticketType = ticketTypeClient.findById(bookingItem.getTicketTypeId());
        return new Ticket(TicketStatus.VALID, ticketType.getPrice(), ticketType.getTaxRate());
    }
}