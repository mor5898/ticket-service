package com.benevolo.rest;

import com.benevolo.entity.Cancellation;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cancellation post(Cancellation cancellation) {
        return cancellationService.save(cancellation);
    }

    @GET
    public List<Cancellation> getAllCancellations() {
        return cancellationService.getAllCancellations();
    }

    @PUT
    @Path("/{cancellationId}/status/redeemed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void redeemCancellationByStatusUpdate(@PathParam("cancellationId") String cancellationId) {
        cancellationService.changeCancellationStatus(cancellationId, true);
    }

    @PUT
    @Path("/{cancellationId}/status/cancelled")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void cancelCancellationByStatusUpdate(@PathParam("cancellationId") String cancellationId) {
        cancellationService.changeCancellationStatus(cancellationId, false);
    }
}
