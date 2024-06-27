package com.benevolo.cancellation;

import com.benevolo.entity.*;
import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.*;
import com.benevolo.service.CancellationService;
import com.benevolo.utils.CancellationStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@TestSecurity(user = "user")
class CancellationTest {

    @Inject
    CancellationRepo cancellationRepo;

    @Inject
    BookingRepo bookingRepo;

    @Inject
    CancellationService cancellationService;

    @BeforeEach
    @Transactional
    void setUp() {
        Event event = new Event("event_id_1", "Event 1", new Address("Street", "City", "State", "Zip"), false);
        Booking booking1 = new Booking();
        booking1.setEventId(event.getId());
        bookingRepo.persist(booking1);

        Cancellation cancellation = new Cancellation();
        cancellation.setStatus(CancellationStatus.PENDING);
        cancellation.setBooking(booking1);
        cancellationRepo.persist(cancellation);

        Event event2 = new Event("event_id_2", "Event 2", new Address("Street", "City", "State", "Zip"), false);
        Booking booking2 = new Booking();
        booking2.setEventId(event2.getId());
        bookingRepo.persist(booking2);

        Cancellation cancellation2 = new Cancellation();
        cancellation2.setStatus(CancellationStatus.PENDING);
        cancellation2.setBooking(booking2);
        cancellationRepo.persist(cancellation2);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        cancellationRepo.deleteAll();
        bookingRepo.deleteAll();
    }

    @Test
    @Transactional
    void testChangeCancellationStatus() {
        Cancellation cancellation = cancellationRepo.findAll().list().get(0);
        cancellationService.changeCancellationStatus(cancellation.getId(), true);
        assertThat(cancellation.getStatus(), is(CancellationStatus.ACCEPTED));

        cancellationService.changeCancellationStatus(cancellation.getId(), false);
        assertThat(cancellation.getStatus(), is(CancellationStatus.DECLINED));
    }

    @Test
    void testChangeCancellationStatus_NotFound() {
        // Verify that a NotFoundException is thrown
        Assertions.assertThrows(NotFoundException.class, () -> {
            cancellationService.changeCancellationStatus("non_existent_id", true);
        });
    }

    @Test
    void testGetAllCancellations() {
        List<Cancellation> cancellations = cancellationService.getAllCancellations();
        assertThat(cancellations.size(), equalTo(2));
    }

    @Test
    @Transactional
    void testRedeemCancellation() {
        Cancellation cancellation = cancellationRepo.findAll().list().get(0);
        given()
                .pathParam("cancellationId", cancellation.getId())
                .when()
                .put("/cancellations/status/redeemed/{cancellationId}")
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    @Transactional
    void testCancelCancellation() {
        Cancellation cancellation = cancellationRepo.findAll().list().get(0);
        given()
                .pathParam("cancellationId", cancellation.getId())
                .when()
                .put("/cancellations/status/cancelled/{cancellationId}")
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    @Transactional
    public void testFindAllByEventId() {
        // Test for event_id_1
        List<Cancellation> cancellationsEvent1 = cancellationRepo.findAllByEventId("event_id_1");
        assertEquals(1, cancellationsEvent1.size());
        assertTrue(cancellationsEvent1.stream().allMatch(c -> "event_id_1".equals(c.getBooking().getEventId())));

        // Test for event_id_2
        List<Cancellation> cancellationsEvent2 = cancellationRepo.findAllByEventId("event_id_2");
        assertEquals(1, cancellationsEvent2.size());
        assertTrue(cancellationsEvent2.stream().allMatch(c -> "event_id_2".equals(c.getBooking().getEventId())));

        // Test for non-existent event_id
        List<Cancellation> cancellationsEvent3 = cancellationRepo.findAllByEventId("non_existent_event_id");
        assertTrue(cancellationsEvent3.isEmpty());
    }
}