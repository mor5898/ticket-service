package com.benevolo.mocks;

import com.benevolo.entity.Address;
import com.benevolo.entity.Event;
import com.benevolo.entity.TicketType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.time.LocalDateTime;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TicketTypeClientMock implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(options().port(8082));
        wireMockServer.start();

        try {
            wireMockServer.stubFor(get(urlMatching("/ticket-types/.*"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                    new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(new TicketType("48935", 2000, 19, "Samstagsticket", LocalDateTime.now(),
                                            LocalDateTime.now(), new Event("55378","Test Event",
                                            new Address("Ingolstädter Straße", "Ingolstadt", "Bayern", "80000")
                                    ))))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        wireMockServer.stubFor(get(urlMatching(".*")).atPriority(10).willReturn(aResponse().proxiedFrom("https://stage.code.quarkus.io/api")));

        return Map.of("quarkus.rest-client.event-service.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
