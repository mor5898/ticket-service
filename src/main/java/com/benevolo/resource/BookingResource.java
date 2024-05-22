package com.benevolo.resource;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.repo.BookingRepo;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/bookings")
public class BookingResource {

    @RestClient
    TicketTypeClient ticketTypeClient;

    private final BookingRepo bookingRepo;

    @Inject
    public BookingResource(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getAll() {
        List<Booking> bookings = bookingRepo.listAll();
        bookings.forEach(booking -> {
            booking.getBookingItems().forEach(bookingItem -> {
                bookingItem.setTicketType(ticketTypeClient.findById(bookingItem.getTicketTypeId()));
            });
        });
        return Booking.listAll();
    }

    @GET
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("bookingId") String bookingId) {
        return Booking.findById(bookingId);
    }
}
