package com.benevolo.resource;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    @Path("/event/{eventId}/{pageIndex}/{pageSize}")
    public List<Booking> getAll(@PathParam("eventId") String eventId, @PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize) {
        List<Booking> bookings = bookingRepo.findByEventId(eventId, pageIndex, pageSize);;
        Map<String, TicketType> ticketTypeMap = ticketTypeClient.findByEventId(eventId).stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        bookings.forEach(booking -> {
            booking.getBookingItems().forEach(bookingItem -> {
                bookingItem.setTicketType(ticketTypeMap.get(bookingItem.getTicketTypeId()));
            });
        });
        httpServerResponse.headers().add("X-Page-Size", String.valueOf(bookingRepo.countByEventId(eventId, pageSize)));
        return bookings;
    }

    @GET
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("bookingId") String bookingId) {
        return Booking.findById(bookingId);
    }

}
