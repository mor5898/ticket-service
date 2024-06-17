package com.benevolo.rest;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Cancellation;
import com.benevolo.repo.CancellationRepo;
import com.benevolo.service.CancellationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/cancellations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CancellationResource {

    @Inject
    CancellationService cancellationService;

    @Inject
    CancellationRepo cancellationRepo;

    @RestClient
    TicketTypeClient ticketTypeClient;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{refundId}")
    public void post(@PathParam("refundId") String refundId, List<String> ticketIds) {
        cancellationService.save(ticketIds, refundId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{eventId}")
    public List<Cancellation> getCancellationsForEvent(@PathParam("eventId") String eventId) {
        /*
        List<Cancellation> result = cancellationRepo.findAllByEventId(eventId);
        result.forEach(item -> {
            item.getTicket().getBookingItem().setTicketType(ticketTypeClient.findById(item.getTicket().getBookingItem().getTicketTypeId()));
        }); 

        return result;
         * 
         * 
         */
        
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
