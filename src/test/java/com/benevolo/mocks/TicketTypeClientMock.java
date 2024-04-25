package com.benevolo.mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.time.LocalDateTime;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TicketTypeClientMock implements  QuarkusTestResourceLifecycleManager{

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start(){
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        try {
            wireMockServer.stubFor(get(urlMatching("/ticket-types/.*"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                    new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString("4e8" /*new TicketTypeDTO("4545", "Samstag", 40, 19, 100, true,
                                            LocalDateTime.of(2024, 12, 24, 1, 1, 1), LocalDateTime.of(2024, 12, 25, 1, 1, 1), "1234")*/)
                            )));
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
