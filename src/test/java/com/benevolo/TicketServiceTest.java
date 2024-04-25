package com.benevolo;

import com.benevolo.entity.Ticket;
import com.benevolo.mocks.TicketTypeClientMock;
import com.benevolo.repo.TicketRepo;
import com.benevolo.service.TicketService;
import com.benevolo.utils.TicketStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.locationtech.jts.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;

@Disabled
@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(TicketTypeClientMock.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketServiceTest {

    private final TicketService ticketService;

    private final TicketRepo ticketRepo;

    @Inject
    public TicketServiceTest(TicketService tS, TicketRepo ticketRepo){
        this.ticketService = tS;
        this.ticketRepo = ticketRepo;
    }

    @Disabled
    @Test
    @Order(1)
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void testSaveTicket() {
        /*
         * Possible to test with an edit of save() method of TicketService Class
         * Use predefined Ticket instead of relying on the TicketTypeService of EventService Repository
         * Test this reference in isolated Test above
         *
         * */
        CustomerDTO cDTO = new CustomerDTO("4444", "customer@mail.de");
        List<BookingDTO> bookingList = new ArrayList<BookingDTO>();
        bookingList.add(new BookingDTO(1, "1111", "666", cDTO));
        //ticketService.save(bookingList);
        ValidatableResponse validatableResponse = given().contentType(ContentType.JSON).body(bookingList).when().post("/tickets").then().statusCode(204);
        Assert.equals(1, ticketRepo.listAll().size());
    }

    @Disabled
    @Test
    @Order(2)
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void testTicketUpdate(){

        List<TicketDTO> testList = ticketService.findByEventId("1111", 0, 20);
        TicketDTO ticketDTO = testList.get(0);
        String ticketID = ticketDTO.id();

        Assert.equals(TicketStatus.PENDING, ticketDTO.status());

        TicketDTO refTicketDTO = new TicketDTO(ticketDTO.id(), "328473", ticketDTO.price(), ticketDTO.taxRate(), TicketStatus.VALID, LocalDateTime.now(), ticketDTO.customer(), ticketDTO.ticketType());

        //ticketService.update(ticketID, refTicketDTO);
        ValidatableResponse validatableResponse = given().contentType(ContentType.JSON).body(refTicketDTO).when().put("/tickets/" + ticketID).then().statusCode(204);
        testList = ticketService.findByEventId("1111", 0, 20);
        Assert.equals(TicketStatus.VALID, testList.get(0).status());
    }

    @Disabled
    @Test
    @Order(3)
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void testTicketFindByEventID(){

        CustomerDTO cDTO = new CustomerDTO("4444", "customer@mail.de");
        List<BookingDTO> bookingList = new ArrayList<BookingDTO>();
        bookingList.add(new BookingDTO(45, "4444", "612", cDTO));
        bookingList.add(new BookingDTO(19, "1234", "015", cDTO));
        bookingList.add(new BookingDTO(22, "4444", "123", cDTO));
        bookingList.add(new BookingDTO(89, "9999", "678", cDTO));

        ValidatableResponse validatableResponse = given().contentType(ContentType.JSON).body(bookingList).when().post("/tickets").then().statusCode(204);

        List<TicketDTO> testListPage0 = ticketService.findByEventId("4444", 0, 20);
        Assert.equals(20, testListPage0.size());
        List<TicketDTO> testListPage1 = ticketService.findByEventId("4444", 1, 20);
        Assert.equals(20, testListPage1.size());
        List<TicketDTO> testListPage2 = ticketService.findByEventId("4444", 2, 20);
        Assert.equals(20, testListPage2.size());
        List<TicketDTO> testListPage3 = ticketService.findByEventId("4444", 3, 20);
        Assert.equals(7, testListPage3.size());
    }

    @Disabled
    @Test
    @Order(5)
    void testTicketFindByEventIDNoEntry(){
        /*
         * Check functionality when called with no tickets in DB
         * Return value of function is expected to be an empty list -> no items inserted
         * */
        List<TicketDTO> testList = ticketService.findByEventId("1234", 0, 20);
        Assert.equals(0, testList.size());
    }


    @Disabled
    @Test
    @Order(3)
    void testTicketUpdate(){
        /*
        * Change the known values of Ticket inserted in Test 1
        * Get with "findByID" and check updated values
        *
        LocalDateTime start = LocalDate.of(2024, 01, 01).atStartOfDay();
        LocalDateTime end = LocalDate.of(2024, 01, 02).atStartOfDay();
        CustomerDTO cDTO = new CustomerDTO("4444", "customer@mail.de");
        TicketTypeDTO ttDTO= new TicketTypeDTO("123", "Samstagticket", 40, 19, 100, true, start, end, "1234");
        TicketStatus tS = TicketStatus.VALID;
        TicketDTO ticketDTO = new TicketDTO("5678", 40, 19, tS, cDTO, ttDTO);

        ticketService.update("5678", ticketDTO);
        List<TicketDTO> testList = ticketService.findByEventId("1234", 0, 20);
        Assert.equals(TicketStatus.VALID, testList.get(0).status());
    }


    @Disabled
    @Test
    @Order(4)
    void testTicketGenerationWithClient(){
        /*
         * Validate return values of TicketDTO -> test TicketTypeClient functionality
         * Implementation unclear - how to reach EventService Database inside test environment
         * */
        Ticket refTE = new Ticket();
        refTE.setPrice(40);
        refTE.setTaxRate(19);
        CustomerDTO cDTO = new CustomerDTO("4444", "customer@mail.de");
        BookingDTO testBooking= new BookingDTO(2, "1111", "MUSS DB REF SEIN / ANDI FRAGEN", cDTO);
        Ticket testEntity = new Ticket();
        //testEntity = ticketService.generateTicket(testBooking);
        Assert.equals(refTE.getPrice(), testEntity.getPrice());
        Assert.equals(refTE.getTaxRate(), testEntity.getTaxRate());
    }

}
