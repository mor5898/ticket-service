package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.*;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class TicketService {

    @RestClient
    TicketTypeClient ticketTypeClient;

    private final TicketRepo ticketRepo;
    private final BookingRepo bookingRepo;

    @Inject
    public TicketService(TicketRepo ticketRepo, BookingRepo bookingRepo) {
        this.ticketRepo = ticketRepo;
        this.bookingRepo = bookingRepo;
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
        if(ticket.getStatus() == TicketStatus.VALID) {
            ticket.setStatus(TicketStatus.REDEEMED);
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

    public List<ObjectNode> getStatsbyWeek(String eventId, String startDate, String endDate) {
        return null;
    }

    public List<ObjectNode> getStatsByDay(String eventId, String startDate, String endDate) {
        LocalDate start =  LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<LocalDate> dates = start.datesUntil(end).toList();
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> statsByDate = new LinkedList<>();

        for(LocalDate date : dates) {
            Long countBookings = bookingRepo.countByDate(eventId, date);
            Long countTickets = ticketRepo.countByDate(eventId, date);

            ObjectNode node = mapper.createObjectNode();
            node.put("label", date.toString());
            node.putObject("data")
                    .put("orders", countBookings)
                    .put("ticketsSold", countTickets);

            statsByDate.add(node);
        }
        return statsByDate;
    }
}