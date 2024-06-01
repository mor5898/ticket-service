package com.benevolo.booking;

import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Customer;
import com.benevolo.entity.Ticket;
import com.benevolo.includes.mocks.TicketTypeClientMock;
import com.benevolo.repo.BookingItemRepo;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
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

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(TicketTypeClientMock.class)
@TestSecurity(user = "user")
public class BookingAnalyticsTest {

    @Inject
    TicketRepo ticketRepo;

    @Inject
    BookingItemRepo bookingItemRepo;

    @Inject
    BookingRepo bookingRepo;

    @Test
    void testTotalValue() {
        {
            ResponseOptions<Response> response = given().get("/bookings/total-price?eventId=event_id");
            assertThat(Integer.valueOf(response.getBody().asString()), is(6000));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/total-price?eventId=event_id_1");
            assertThat(Integer.valueOf(response.getBody().asString()), is(300000));
        }
    }

    @Test
    void testTotalBookings() {
        {
            ResponseOptions<Response> response = given().get("/bookings/total-bookings?eventId=event_id");
            assertThat(Integer.valueOf(response.getBody().asString()), is(3));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/total-bookings?eventId=event_id_1");
            assertThat(Integer.valueOf(response.getBody().asString()), is(2));
        }
    }

    @Test
    void testAveragePrice() {
        {
            ResponseOptions<Response> response = given().get("/bookings/average-price?eventId=event_id");
            assertThat(Integer.valueOf(response.getBody().asString()), is(2000));
        }
        {
            ResponseOptions<Response> response = given().get("/bookings/average-price?eventId=event_id_1");
            assertThat(Integer.valueOf(response.getBody().asString()), is(150000));
        }
    }

    @Test
    void testTotalTickets() {
        {
            ResponseOptions<Response> response = given().get("/bookings/total-tickets?eventId=event_id");
            assertThat(Integer.valueOf(response.getBody().asString()), is(5));
        }
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        {
            Booking booking = new Booking();
            booking.setTotalPrice(1000);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id1", "jan.h@mail.de"));
            booking.setBookedAt(LocalDate.of(2024, 5, 17).atStartOfDay());
                BookingItem bookingItem = new BookingItem();
                bookingItem.setBooking(booking);
                bookingItem.setQuantity(3);
                    Ticket ticket = new Ticket();
                    ticket.setBookingItem(bookingItem);
                    ticket.setPublicId("485784");
                    ticket.setStatus(TicketStatus.VALID);
                    ticket.setPrice(2000);
                    ticket.setTaxRate(19);
                    Ticket ticket2 = new Ticket();
                    ticket2.setBookingItem(bookingItem);
                    ticket2.setPublicId("485784");
                    ticket2.setStatus(TicketStatus.VALID);
                    ticket2.setPrice(2000);
                    ticket2.setTaxRate(19);
                    Ticket ticket3 = new Ticket();
                    ticket3.setBookingItem(bookingItem);
                    ticket3.setPublicId("485784");
                    ticket3.setStatus(TicketStatus.VALID);
                    ticket3.setPrice(2000);
                    ticket3.setTaxRate(19);
                bookingItem.setTickets(List.of(ticket, ticket2, ticket3));
            booking.setBookingItems(List.of(bookingItem));
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(2000);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id2", "andreas.d@t-online.de"));
            booking.setBookedAt(LocalDate.of(2024, 5, 19).atStartOfDay());
            BookingItem bookingItem = new BookingItem();
                bookingItem.setQuantity(2);
                bookingItem.setBooking(booking);
                    Ticket ticket = new Ticket();
                    ticket.setBookingItem(bookingItem);
                    ticket.setPublicId("485784");
                    ticket.setStatus(TicketStatus.VALID);
                    ticket.setPrice(2000);
                    ticket.setTaxRate(19);
                    Ticket ticket2 = new Ticket();
                    ticket2.setBookingItem(bookingItem);
                    ticket2.setPublicId("485784");
                    ticket2.setStatus(TicketStatus.VALID);
                    ticket2.setPrice(2000);
                    ticket2.setTaxRate(19);
                bookingItem.setTickets(List.of(ticket, ticket2));
            booking.setBookingItems(List.of(bookingItem));
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(3000);
            booking.setEventId("event_id");
            booking.setCustomer(new Customer("stripe_id3", "felix.s@gmail.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 21).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(100000);
            booking.setEventId("event_id_1");
            booking.setCustomer(new Customer("stripe_id3", "max.s@gmail.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 21).atStartOfDay());
            bookingRepo.persist(booking);
        }
        {
            Booking booking = new Booking();
            booking.setTotalPrice(200000);
            booking.setEventId("event_id_1");
            booking.setCustomer(new Customer("stripe_id3", "andy.s@gmail.com"));
            booking.setBookedAt(LocalDate.of(2024, 5, 21).atStartOfDay());
            bookingRepo.persist(booking);
        }
    }

    @AfterEach
    @Transactional
    void afterEach() {
        ticketRepo.deleteAll();
        bookingItemRepo.deleteAll();
        bookingRepo.deleteAll();
    }

}
