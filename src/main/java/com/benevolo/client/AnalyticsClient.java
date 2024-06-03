package com.benevolo.client;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterClientHeaders
@RegisterRestClient(configKey = "analytics-service")
public interface AnalyticsClient {

    @POST
    @Path("/validated-tickets/event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    void createEntryHistory(@PathParam("eventId") String eventId);

}
