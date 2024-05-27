package com.benevolo.resource;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/bookings")
public class BookingResource {

    @RestClient
    TicketTypeClient ticketTypeClient;
    private final HttpServerResponse httpServerResponse;

    private final BookingRepo bookingRepo;

    @Inject
    public BookingResource(BookingRepo bookingRepo, HttpServerResponse httpServerResponse
    ) {
        this.bookingRepo = bookingRepo;
        this.httpServerResponse = httpServerResponse;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pageIndex}")
    public List<Booking> getAll(@QueryParam("eventId") String eventId, @PathParam("pageIndex") Integer pageIndex) {
        final int PAGE_SIZE = 15;
        List<Booking> bookings = bookingRepo.findByEventId(eventId, pageIndex, PAGE_SIZE);
        Map<String, TicketType> ticketTypeMap = ticketTypeClient.findByEventId(eventId).stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        bookings.forEach(booking -> {
            booking.getBookingItems().forEach(bookingItem -> {
                bookingItem.setTicketType(ticketTypeMap.get(bookingItem.getTicketTypeId()));
            });
        });
        httpServerResponse.headers().add("X-Page-Size", String.valueOf(bookingRepo.countByEventId(eventId, PAGE_SIZE)));
        return bookings;
    }

    @GET
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("bookingId") String bookingId) {
        return Booking.findById(bookingId);
    }

}
