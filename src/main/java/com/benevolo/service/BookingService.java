package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.RefundLink;
import com.benevolo.repo.BookingRepo;
import com.benevolo.rest.params.BookingSearchParams;
import com.benevolo.utils.query_builder.QueryBuilder;
import com.benevolo.utils.query_builder.section.QueryCustomSection;
import com.benevolo.utils.query_builder.section.QuerySection;
import com.benevolo.utils.query_builder.section.order_by.OrderBySection;
import com.benevolo.utils.query_builder.section.order_by.OrderType;
import com.benevolo.utils.query_builder.util.Compartor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        booking.setRefundLink(new RefundLink());

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
        return getQueryBuilder(eventId, params)
                .orderBy(OrderBySection.of("bookedAt", OrderType.DESC))
                .find(bookingRepo)
                .page(pageIndex, pageSize)
                .list();
    }

    public Long countByEventIdAndSearch(String eventId, int pageSize, BookingSearchParams params) {
        Long results = getQueryBuilder(eventId, params).count(bookingRepo);
        return (long) Math.ceil((results * 1.0) / pageSize);
    }

    private QueryBuilder<Booking> getQueryBuilder(String eventId, BookingSearchParams params) {
        QueryBuilder<Booking> queryBuilder = QueryBuilder.build();

        queryBuilder.add(QuerySection.of("eventId", Compartor.EQUAlS, eventId));

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

        Integer priceFrom = params.priceFrom;
        if(priceFrom != null) {
            queryBuilder.add(QuerySection.of("totalPrice", Compartor.GREATER_THAN_OR_EQUALS, Math.round((priceFrom*1.0F)*100)));
        }

        Integer priceTo = params.priceTo;
        if(priceTo != null) {
            queryBuilder.add(QuerySection.of("totalPrice", Compartor.LESS_THAN_OR_EQUALS, Math.round((priceTo*1.0F)*100)));
        }

        return queryBuilder;
    }

    public RefundLink findRefundLinkByBooking(Booking booking) {
        return booking.getRefundLink();
    }

}
