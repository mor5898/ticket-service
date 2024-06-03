package com.benevolo.client;

import com.benevolo.entity.TicketType;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterClientHeaders
@RegisterRestClient(configKey = "analytics-service")
public interface AnalyticsClient {

    @POST
    @Path("/validated-tickets/event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<TicketType> createEntryHistory(@PathParam("eventId") String eventId);

}
