package com.benevolo.resource;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.service.BookingService;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/bookings")
public class BookingResource {

    @RestClient
    TicketTypeClient ticketTypeClient;
    private final HttpServerResponse httpServerResponse;

    private final BookingService bookingService;

    private final BookingRepo bookingRepo;

    private final TicketRepo ticketRepo;

    private final UriInfo uriInfo;

    @Inject
    public BookingResource(BookingRepo bookingRepo, HttpServerResponse httpServerResponse,
                           BookingService bookingService, TicketRepo ticketRepo, UriInfo uriInfo) {
        this.bookingRepo = bookingRepo;
        this.httpServerResponse = httpServerResponse;
        this.bookingService = bookingService;
        this.ticketRepo = ticketRepo;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pageIndex}")
    public List<Booking> getAll(@QueryParam("eventId") String eventId,
                                @PathParam("pageIndex") Integer pageIndex,
                                @QueryParam("term") String term,
                                @QueryParam("bookedFrom") String bookedFrom) {
        final int PAGE_SIZE = 15;
        List<Booking> bookings = bookingService.findByEventIdAndSearch(eventId, pageIndex, uriInfo.getQueryParameters());
        Map<String, TicketType> ticketTypeMap = ticketTypeClient.findByEventId(eventId).stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        bookings.forEach(booking -> {
            booking.getBookingItems().forEach(bookingItem -> {
                bookingItem.setTicketType(ticketTypeMap.get(bookingItem.getTicketTypeId()));
            });
        });
        httpServerResponse.headers().add("X-Page-Size", String.valueOf(bookingRepo.countPagesByEventId(eventId, PAGE_SIZE)));
        return bookings;
    }

    @GET
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("bookingId") String bookingId) {
        return Booking.findById(bookingId);
    }

    @GET
    @Path("/total-price")
    @Produces(MediaType.TEXT_PLAIN)
    public Long getTotalPrice(@QueryParam("eventId") String eventId) {
        return bookingRepo.findPriceByEventId(eventId);
    }

    @GET
    @Path("/average-price")
    @Produces(MediaType.TEXT_PLAIN)
    public Long getAveragePrice(@QueryParam("eventId") String eventId) {
        return bookingRepo.findAveragePriceByEventId(eventId);
    }

    @GET
    @Path("/total-bookings")
    @Produces(MediaType.TEXT_PLAIN)
    public Long getTotalBookings(@QueryParam("eventId") String eventId) {
        return bookingRepo.countByEventId(eventId);
    }

    @GET
    @Path("/total-tickets")
    @Produces(MediaType.TEXT_PLAIN)
    public Long getTotalTickets(@QueryParam("eventId") String eventId) {
        return ticketRepo.countByEventId(eventId);
    }

}
