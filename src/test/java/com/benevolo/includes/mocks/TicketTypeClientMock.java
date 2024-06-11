package com.benevolo.includes.mocks;

import com.benevolo.entity.Address;
import com.benevolo.entity.Event;
import com.benevolo.entity.TicketType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class TicketTypeClientMock implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;
    private final List<TicketType> ticketTypes;

    public TicketTypeClientMock() {
        ticketTypes = new LinkedList<>();
        ticketTypes.add(new TicketType(
                "02e6ee0d-4a79-4d20-a115-eb389b50175c",
                2100,
                19,
                "Samstagsticket",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                new Event("event_id", "Test Event", new Address("Ingolstädter Straße", "Ingolstadt", "Bayern", "85049"), false)
        ));
        ticketTypes.add(new TicketType(
                "439575ed-8691-4fe1-97ff-e73332d0a379",
                2300,
                19,
                "Sonntagsticket",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                new Event("event_id", "Test Event", new Address("Ingolstädter Straße", "Ingolstadt", "Bayern", "85290"), false)
        ));
        ticketTypes.add(new TicketType(
                "a0f22577-6231-4323-afee-1847d6c53278",
                4500,
                19,
                "Wochenendticket",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                new Event("event_id", "Test Event", new Address("Ingolstädter Straße", "Ingolstadt", "Bayern", "85119"), false)
        ));
    }

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(options().port(8082));
        wireMockServer.start();

        try {
            wireMockServer.stubFor(get(urlPathEqualTo("/ticket-types"))
                    .withQueryParam("eventId", equalTo("event_id"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(generateResponse(ticketTypes))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        wireMockServer.stubFor(get(urlPathEqualTo("/ticket-types"))
                .withQueryParam("eventId", matching(".*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        try {
            wireMockServer.stubFor(get(urlPathEqualTo("/ticket-types/02e6ee0d-4a79-4d20-a115-eb389b50175c"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(generateResponse(ticketTypes.get(0)))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            wireMockServer.stubFor(get(urlPathEqualTo("/ticket-types/439575ed-8691-4fe1-97ff-e73332d0a379"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(generateResponse(ticketTypes.get(1)))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            wireMockServer.stubFor(get(urlPathEqualTo("/ticket-types/a0f22577-6231-4323-afee-1847d6c53278"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(generateResponse(ticketTypes.get(2)))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Map.of("quarkus.rest-client.event-service.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }

    private String generateResponse(Object object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }
}
