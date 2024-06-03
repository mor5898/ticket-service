package com.benevolo.ticket;

import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Ticket;
import com.benevolo.includes.mocks.AnalyticsClientMock;
import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.BookingItemRepo;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import io.quarkus.panache.common.Parameters;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@QuarkusTestResource(AnalyticsClientMock.class)
@TestSecurity(user = "user")
class TicketRedeemTest {

    @Inject
    TicketRepo ticketRepo;

    @Inject
    BookingItemRepo bookingItemRepo;

    @Inject
    BookingRepo bookingRepo;

    @Inject
    EntityManager em;

    @Test
    void test() throws IOException {
        Ticket validTicket = ticketRepo.find("status = :status", Parameters.with("status", TicketStatus.VALID)).list().getFirst();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("/request_bodies/ticket/ticket_update.json"))
                .when()
                .put("/tickets/" + validTicket.getId() + "/status")
                .then()
                .statusCode(204);

        Ticket redeemedTicket = ticketRepo.find("status = :status", Parameters.with("status", TicketStatus.REDEEMED)).list().getFirst();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("/request_bodies/ticket/ticket_update.json"))
                .when()
                .put("/tickets/" + redeemedTicket.getId() + "/status")
                .then()
                .statusCode(400);

        Ticket cancelledTicket = ticketRepo.find("status = :status", Parameters.with("status", TicketStatus.CANCELLED)).list().getFirst();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("/request_bodies/ticket/ticket_update.json"))
                .when()
                .put("/tickets/" + cancelledTicket.getId() + "/status")
                .then()
                .statusCode(400);

        em.clear();

        assertThat(ticketRepo.count("status = :status", Parameters.with("status", TicketStatus.REDEEMED)), is(2L));
        assertThat(ticketRepo.count("status = :status", Parameters.with("status", TicketStatus.CANCELLED)), is(1L));
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        Booking booking = new Booking();
        booking.setTotalPrice(10500);
        booking.setEventId("event_id");
        bookingRepo.persist(booking);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setQuantity(3);
        bookingItem.setId("12345");
        bookingItem.setBooking(booking);
        bookingItem = em.merge(bookingItem);
        bookingItemRepo.persist(bookingItem);

        String[] publicIds = {"94385", "17385", "62385"};
        TicketStatus[] statuses = {TicketStatus.VALID, TicketStatus.REDEEMED, TicketStatus.CANCELLED};

        for (int i = 0; i < publicIds.length; i++) {
            Ticket ticket = new Ticket();
            ticket.setPublicId(publicIds[i]);
            ticket.setStatus(statuses[i]);
            ticket.setPrice(3500);
            ticket.setTaxRate(19);
            ticket.setBookingItem(bookingItem);
            ticketRepo.persist(ticket);
        }
    }

    @AfterEach
    @Transactional
    void afterEach() {
        ticketRepo.deleteAll();
    }

}
