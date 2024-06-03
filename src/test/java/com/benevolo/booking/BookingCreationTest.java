package com.benevolo.booking;

import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.BookingItemRepo;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.benevolo.includes.utils.FileReader.read;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@TestSecurity(user = "user")
class BookingCreationTest {

    @Inject
    BookingRepo bookingRepo;

    @Inject
    BookingItemRepo bookingItemRepo;

    @Inject
    TicketRepo ticketRepo;

    @Inject
    EntityManager em;

    @Test
    void test() throws IOException {

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("request_bodies/booking/booking_1.json")).
                when()
                .post("/bookings")
                .then()
                .statusCode(200);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("request_bodies/booking/booking_2.json")).
                when()
                .post("/bookings")
                .then()
                .statusCode(500);

        assertThat(bookingRepo.count(), is(1L));
        assertThat(bookingItemRepo.count(), is(3L));
        assertThat(ticketRepo.count(), is(9L));
    }

    @AfterEach
    @Transactional
    void afterEach() {
        ticketRepo.deleteAll();
        bookingItemRepo.deleteAll();
        bookingRepo.deleteAll();
    }

}
