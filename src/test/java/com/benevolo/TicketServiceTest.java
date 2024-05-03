package com.benevolo;

import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Customer;
import com.benevolo.entity.Ticket;
import com.benevolo.mocks.TicketTypeClientMock;
import com.benevolo.repo.BookingItemRepo;
import com.benevolo.repo.BookingRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import org.locationtech.jts.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(TicketTypeClientMock.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity(authorizationEnabled = false)
public class TicketServiceTest {

    private final TicketRepo ticketRepo;
    private final BookingRepo bookingRepo;
    private final BookingItemRepo bookingItemRepo;

    @Inject
    public TicketServiceTest(TicketRepo ticketRepo, BookingRepo bookingRepo, BookingItemRepo bookingItemRepo){
        this.ticketRepo = ticketRepo;
        this.bookingRepo = bookingRepo;
        this.bookingItemRepo = bookingItemRepo;
    }

    @Test
    @Order(1)
    void testSaveTicket() {
        Customer customer = new Customer("3948394", "test@mail.de");
        Booking booking = new Booking();
        List<BookingItem> bookingItems = new LinkedList<>();
            BookingItem bookingItem0 = new BookingItem();
            bookingItem0.setBooking(booking);
            bookingItem0.setQuantity(2);
            bookingItem0.setTicketTypeId("394893");
            BookingItem bookingItem1 = new BookingItem();
            bookingItem1.setBooking(booking);
            bookingItem1.setQuantity(3);
            bookingItem1.setTicketTypeId("758323");
        bookingItems.add(bookingItem0);
        bookingItems.add(bookingItem1);
        booking.setCustomer(customer);
        booking.setBookedAt(LocalDateTime.now());
        booking.setEventId("eventId");
        booking.setBookingItems(bookingItems);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(booking)
                .when()
                .post("/tickets")
                .then()
                .statusCode(204);

        Assert.equals(1, bookingRepo.listAll().size());
        Assert.equals(2, bookingItemRepo.listAll().size());
        Assert.equals(5, ticketRepo.listAll().size());
        Assert.equals(6, ticketRepo.listAll().getFirst().getPublicId().length());
    }

    @Test
    @Order(2)
    void testUpdateTicket() {
        Ticket ticket = ticketRepo.listAll().getFirst();
        ticket.setStatus(TicketStatus.REDEEMED);
        ticket.setPrice(69_420);
        ticket.setTaxRate(40);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(ticket)
                .when()
                .put("/tickets/" + ticket.getId())
                .then()
                .statusCode(204);

        Assert.equals(TicketStatus.REDEEMED, ticketRepo.findById(ticket.getId()).getStatus());
        Assert.equals(69_420, ticketRepo.findById(ticket.getId()).getPrice());
        Assert.equals(40, ticketRepo.findById(ticket.getId()).getTaxRate());
    }

    @Test
    @Order(3)
    void testRedeemTicket() {
        {
            Ticket ticket = ticketRepo.list("status", TicketStatus.VALID).getFirst();

            given().when()
                    .patch("/tickets/" + ticket.getId() + "/status")
                    .then()
                    .statusCode(204);

            Assert.equals(2, ticketRepo.list("status", TicketStatus.REDEEMED).size());
        }
        {
            Ticket ticket = ticketRepo.list("status", TicketStatus.REDEEMED).getFirst();

            given().when()
                    .patch("/tickets/" + ticket.getId() + "/status")
                    .then()
                    .statusCode(400);

            Assert.equals(TicketStatus.REDEEMED, ticketRepo.findById(ticket.getId()).getStatus());
        }
    }



    @Test
    @Order(4)
    void testGetTickets() throws JsonProcessingException {
        {
            Map<String, String> expectedHeaders = new HashMap<>();
            expectedHeaders.put("X-Page-Size", "1");

            Response response = given().when().get("/events/eventId/tickets/0/10");
            response.then().statusCode(200);
            response.then().headers(expectedHeaders);

            List<Ticket> tickets = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response.getBody().asString(), new TypeReference<>() {});
            Assert.equals(5, tickets.size());
        }
        {
            Map<String, String> expectedHeaders = new HashMap<>();
            expectedHeaders.put("X-Page-Size", "2");

            {
                Response response = given().when().get("/events/eventId/tickets/0/3");
                response.then().statusCode(200);
                response.then().headers(expectedHeaders);

                List<Ticket> tickets = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response.getBody().asString(), new TypeReference<>() {});
                Assert.equals(3, tickets.size());
            }
            {
                Response response = given().when().get("/events/eventId/tickets/1/3");
                response.then().statusCode(200);
                response.then().headers(expectedHeaders);

                List<Ticket> tickets = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response.getBody().asString(), new TypeReference<>() {});
                Assert.equals(2, tickets.size());
            }
        }
        {
            Map<String, String> expectedHeaders = new HashMap<>();
            expectedHeaders.put("X-Page-Size", "5");

            Response response = given().when().get("/events/eventId/tickets/0/1");
            response.then().statusCode(200);
            response.then().headers(expectedHeaders);

            List<Ticket> tickets = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(response.getBody().asString(), new TypeReference<>() {});
            Assert.equals(1, tickets.size());
        }
    }
}
