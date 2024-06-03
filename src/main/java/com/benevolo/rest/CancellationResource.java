package com.benevolo.rest;

import com.benevolo.entity.Cancellation;
import com.benevolo.repo.CancellationRepo;
import com.benevolo.service.CancellationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/cancellation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CancellationResource {

    @Inject
    CancellationService cancellationService;

    @Inject
    CancellationRepo cancellationRepo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingId}")
    public Cancellation post(@PathParam("bookingId") String bookingId, Cancellation cancellation) {
        return cancellationService.save(cancellation, bookingId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{eventId}")
    public List<Cancellation> getCancellationsForEvent(@PathParam("eventId") String eventId) {
        return cancellationRepo.findAllByEventId(eventId);
    }

    @GET
    public List<Cancellation> getAllCancellations() {
        return cancellationService.getAllCancellations();
    }

    @PUT
    @Path("/status/redeemed/{cancellationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void redeemCancellationByStatusUpdate(@PathParam("cancellationId") String cancellationId) {
        cancellationService.changeCancellationStatus(cancellationId, true);
    }

    @PUT
    @Path("/status/cancelled/{cancellationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void cancelCancellationByStatusUpdate(@PathParam("cancellationId") String cancellationId) {
        cancellationService.changeCancellationStatus(cancellationId, false);
    }
}
