package com.benevolo.client;

import com.benevolo.dto.TicketTypeDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ticket-types")
@RegisterRestClient
public interface TicketTypeClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TicketTypeDTO findById(@PathParam("id") String id);

}
