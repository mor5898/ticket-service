package com.benevolo.ticket;

import com.benevolo.entity.Ticket;
import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.benevolo.includes.utils.FileReader.read;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@TestSecurity(user = "user")
public class TicketUpdateTest {

    @Inject
    TicketRepo ticketRepo;

    @Inject
    EntityManager em;

    @Test
    void test() throws IOException {
        Ticket ticket = ticketRepo.listAll().getFirst();
        assert ticket != null;

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(read("/request_bodies/ticket/ticket_update.json"))
        .when()
            .put("/tickets/" + ticket.getId())
        .then()
            .statusCode(204);

        em.clear();

        ticket = ticketRepo.listAll().getFirst();

        assertEquals(TicketStatus.REDEEMED, ticket.getStatus());
        assertEquals(2000, ticket.getPrice());
        assertEquals(7, ticket.getTaxRate());
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        Ticket ticket = new Ticket();
        ticket.setPublicId("94385");
        ticket.setStatus(TicketStatus.VALID);
        ticket.setPrice(3500);
        ticket.setTaxRate(19);
        ticketRepo.persist(ticket);
    }

    @AfterEach
    @Transactional
    void afterEach() {
        ticketRepo.deleteAll();
    }

}
