package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.repo.BookingRepo;
import com.benevolo.rest.params.BookingSearchParams;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepo bookingRepo;

    @Inject
    TicketService ticketService;

    @RestClient
    TicketTypeClient ticketTypeClient;

    @Transactional
    public Booking save(Booking booking) {
        for (BookingItem bookingItem : booking.getBookingItems()) {
            bookingItem.setBooking(booking);
            booking.setBookedAt(LocalDateTime.now());
            bookingItem.setTicketType(ticketTypeClient.findById(bookingItem.getTicketTypeId()));
            bookingItem.setTickets(new LinkedList<>());
            for (int i = 0; i < bookingItem.getQuantity(); i++) {
                bookingItem.addTicket(ticketService.generateTicket(bookingItem));
            }
        }
        booking.setTotalPrice(calculateTotal(booking.getBookingItems()));
        Booking.persist(booking);
        return booking;
    }

    private int calculateTotal(List<BookingItem> bookingItems) {
        int total = 0;
        for(BookingItem bookingItem: bookingItems) {
            total += bookingItem.getTicketType().getPrice() * bookingItem.getQuantity();
        }
        return total;
    }

    public List<Booking> findByEventIdAndSearch(String eventId, int pageIndex, int pageSize, BookingSearchParams params) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        query.append("SELECT b FROM Booking AS b WHERE b.eventId = :eventId");
        parameters.put("eventId", eventId);

        String term = params.term;
        if(term != null && !term.isBlank()) {
            term = "%" + term + "%";
            query.append(" and (LOWER(b.id) LIKE :term OR LOWER(b.customer.email) LIKE :term)");
            parameters.put("term", term.toLowerCase());
        }

        LocalDate dateFrom = params.dateFrom;
        if(dateFrom != null) {
            query.append(" and b.bookedAt >= :bookedFrom");
            parameters.put("bookedFrom", dateFrom.atStartOfDay());
        }

        LocalDate dateTo = params.dateTo;
        if(dateTo != null) {
            query.append(" and b.bookedAt < :bookedTo");
            parameters.put("bookedTo", dateTo.plusDays(1).atStartOfDay());
        }

        Integer priceFrom = params.priceFrom;
        if(priceFrom != null) {
            query.append(" and b.totalPrice >= :priceFrom");
            parameters.put("priceFrom", priceFrom);
        }

        Integer priceTo = params.priceTo;
        if(priceTo != null) {
            query.append(" and b.totalPrice <= :priceTo");
            parameters.put("priceTo", priceTo);
        }

        query.append(" ORDER BY b.bookedAt DESC");

        return bookingRepo.find(query.toString(), parameters).page(pageIndex, pageSize).list();
    }

}
