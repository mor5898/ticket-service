package com.benevolo.client;

import com.benevolo.dto.TicketTypeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "event-service")
public interface TicketTypeClient {

    @GET
    @Path("/ticket-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TicketTypeDTO findById(@HeaderParam("Authorization") String token, @PathParam("id") String id);

}
