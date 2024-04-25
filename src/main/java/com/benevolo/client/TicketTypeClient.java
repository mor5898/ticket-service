package com.benevolo.client;

import com.benevolo.entity.TicketType;
import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@AccessToken
@RegisterRestClient(configKey = "event-service")
public interface TicketTypeClient {

    @GET
    @Path("/ticket-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TicketType findById(@PathParam("id") String id);

}