package com.benevolo.client;

import com.benevolo.entity.TicketType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@RegisterClientHeaders
@RegisterRestClient(configKey = "event-service")
public interface TicketTypeClient {

    @GET
    @Path("/ticket-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TicketType findById(@PathParam("id") String id);

    @GET
    @Path("/ticket-types")
    @Produces(MediaType.APPLICATION_JSON)
    List<TicketType> findByEventId(@RestQuery String eventId);

}