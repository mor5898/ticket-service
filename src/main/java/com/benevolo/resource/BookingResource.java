package com.benevolo.resource;

import com.benevolo.entity.Booking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/bookings")
public class BookingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getAll() {
        return Booking.listAll();
    }

    @GET
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("bookingId") String bookingId) {
        return Booking.findById(bookingId);
    }
}
