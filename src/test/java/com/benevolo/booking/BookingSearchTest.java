package com.benevolo.booking;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.BookingRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@TestSecurity(user = "user")
public class BookingSearchTest {

    @Inject
    BookingRepo bookingRepo;

    @Test
    void test() throws IOException {
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id&priceFrom=8500&priceTo=10500");
            assertThat(responseToBookings(response).size(), is(3));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id&dateFrom=2024-05-19&dateTo=2024-05-23");
            assertThat(responseToBookings(response).size(), is(3));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id&term=andreas");
            assertThat(responseToBookings(response).size(), is(2));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id&term=gmx.com");
            assertThat(responseToBookings(response).size(), is(3));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id_1&term=max");
            assertThat(responseToBookings(response).size(), is(1));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/0?eventId=event_id_1");
            assertThat(responseToBookings(response).size(), is(2));
        }
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        {
            Booking booking = new Booking();
            booking.setTotalPrice(12000);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id1", "jan.h@mail.de"));
            booking.setBookedAt(LocalDate.of(2024, 5, 17).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(4500);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id2", "andreas.d@t-online.de"));
            booking.setBookedAt(LocalDate.of(2024, 5, 19).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(8500);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id3", "felix.s@gmail.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 21).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(9500);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id3", "andreas.o@gmx.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 23).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(13300);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id3", "bernd.o@gmx.com"));
            booking.setBookedAt(LocalDate.of(2024, 6, 25).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(10500);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id3", "hannes.o@gmx.com"));
            booking.setBookedAt(LocalDate.of(2024, 6, 29).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(13450);
            booking.setEventId("event_id_1");
            booking.setCustomer(new Customer("stripe_id3", "fred.o@gmx.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 23).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(12200);
            booking.setEventId("event_id_1");
            booking.setCustomer(new Customer("stripe_id3", "max.o@gmx.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 25).atStartOfDay());
            bookingRepo.persist(booking);
        }
    }

    @AfterEach
    @Transactional
    void afterEach() {
        bookingRepo.deleteAll();
    }

    private List<Booking> responseToBookings(ResponseOptions<Response> response) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response.getBody().print(), new TypeReference<List<Booking>>() {});
    }

}
