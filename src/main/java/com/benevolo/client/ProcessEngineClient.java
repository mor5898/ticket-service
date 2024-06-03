package com.benevolo.client;

import com.benevolo.dto.TicketRedeemDTO;
import io.quarkus.rest.client.reactive.ClientQueryParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterClientHeaders
@RegisterRestClient(configKey = "process-engine")

public interface ProcessEngineClient {

    @POST
    @Path("/atlas_engine/api/v1/messages/TicketScannen/trigger")
    @Consumes(MediaType.APPLICATION_JSON)
    void startTicketRedeemProcess(TicketRedeemDTO ticketRedeemDTO);
}
