package com.benevolo.service;

import com.benevolo.client.AnalyticsClient;
import com.benevolo.client.TicketTypeClient;
import com.benevolo.dto.StatsDTO;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Ticket;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.RefundLinkRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.rest.params.BookingSearchParams;
import com.benevolo.utils.TicketStatus;
import com.benevolo.utils.query_builder.QueryBuilder;
import com.benevolo.utils.query_builder.section.QueryCustomSection;
import com.benevolo.utils.query_builder.section.QuerySection;
import com.benevolo.utils.query_builder.util.Compartor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TicketService {

    @RestClient
    TicketTypeClient ticketTypeClient;

    @RestClient
    AnalyticsClient analyticsClient;

    @Inject
    TicketRepo ticketRepo;

    @Inject
    BookingRepo bookingRepo;

    @Inject
    RefundLinkRepo refundLinkRepo;

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
            // Save Analytics data and get event id for Ticket
            String eventId = ticket.getBookingItem().getBooking().getEventId();
            analyticsClient.createEntryHistory(eventId);
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

    public List<Ticket> findByRefundId(String refundId) {
        List<Ticket> result = new LinkedList<>();
        for(BookingItem bookingItem : bookingRepo.findById(refundLinkRepo.findById(refundId).getBooking().getId()).getBookingItems()) {
            result.addAll(bookingItem.getTickets());
        }
        return result;
    }

    public List<Ticket> findBySearch(String eventId, int pageIndex, int pageSize, BookingSearchParams params) {
        QueryBuilder<Ticket> queryBuilder = getQueryBuilder(eventId, params);
        return queryBuilder.find(ticketRepo).page(pageIndex, pageSize).list();
    }

    private QueryBuilder<Ticket> getQueryBuilder(String eventId, BookingSearchParams params) {
        QueryBuilder<Ticket> queryBuilder = QueryBuilder.build();

        queryBuilder.head("SELECT t FROM Ticket AS t, Booking AS b, BookingItem AS bi");

        String term = params.term;
        if(term != null && !term.isBlank()) {
            queryBuilder.add(QueryCustomSection.of("LOWER(id) LIKE :term OR LOWER(customer.email) LIKE :term", Map.of("term", "%" + term + "%")));
        }

        LocalDate dateFrom = params.dateFrom;
        if(dateFrom != null) {
            queryBuilder.add(QuerySection.of("bookedAt", Compartor.GREATER_THAN_OR_EQUALS, params.dateFrom.atStartOfDay()));
        }

        LocalDate dateTo = params.dateTo;
        if(dateTo != null) {
            queryBuilder.add(QuerySection.of("bookedAt", Compartor.LESS_THAN, dateTo.plusDays(1).atStartOfDay()));
        }

        queryBuilder.add(QuerySection.of("totalPrice", Compartor.GREATER_THAN_OR_EQUALS, params.priceFrom));
        queryBuilder.add(QuerySection.of("totalPrice", Compartor.LESS_THAN_OR_EQUALS, params.priceTo));

        queryBuilder.add(QueryCustomSection.of("t.bookingItem = bi AND bi.booking = b AND b.eventId = :eventId", Map.of("eventId", eventId)));

        return queryBuilder;
    }
}