package com.benevolo;

import com.benevolo.entity.*;
import com.benevolo.repo.BookingRepo;
import com.benevolo.utils.TicketStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

@Disabled
@QuarkusTest
@TestSecurity(user = "user")
public class TestDataGenerator {

    @Inject
    BookingRepo bookingRepo;

    private final List<Integer> randomInt = List.of(1, 2, 2, 2, 3, 3, 4, 5, 6);
    private final List<String> eventIds = List.of("383f700f-5449-4e40-b509-bee0b5d139d6", "aa34700f-5449-4e40-b509-bee0b5d139d6");
    private final List<String> firstNames = List.of("Andreas", "Felix", "Levi", "Liam", "Lina", "Emil", "Anton", "Emilia", "Theo", "Paul", "Emma", "Leano", "Noah", "Matteo", "Sophia", "Hannah", "Elias", "Jan", "Linus", "Ramon", "Mia", "Paul", "Lina", "Finn", "Liam");
    private final List<String> lastNames = List.of("Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Schulz", "Becker", "Hoffmann", "Schäfer", "Koch", "Richter", "Bauer", "Klein", "Wolf", "Schröder", "Neumann", "Schwarz", "Zimmermann", "Braun", "Hofmann", "Krüger", "Hartmann", "Lange", "Schmitt", "Werner");
    private final List<String> mailEnding = List.of("gmail.com", "gmx.com", "outlook.com", "protonmail.com", "freenet.de", "mail.de", "yahoo.com");
    private final List<TicketStatus> ticketStatus = List.of(TicketStatus.VALID, TicketStatus.CANCELLED, TicketStatus.REDEEMED);
    private final Map<String, List<TicketType>> ticketTypes = getTicketTypes();


    @Test
    public void test() {
        System.out.println(bookingRepo.count());
    }

    @BeforeEach
    @Transactional
    public void before() {
        for(int i = 0; i < 2000; i++) {
            bookingRepo.persistAndFlush(generateBooking());
        }
    }

    private Booking generateBooking() {
        String eventId = eventIds.get(new Random().nextInt(eventIds.size()));

        Booking booking = new Booking();
        booking.setEventId(eventId);
        booking.setBookedAt(LocalDateTime.now().minusDays(new Random().nextInt(200)));
        booking.setCustomer(generateCustomer(booking));

        List<BookingItem> bookingItems = new LinkedList<>();
        for(int i = 0; i < randomInt.get(new Random().nextInt(randomInt.size())); i++) {
            bookingItems.add(generateBookingItem(booking));
        }
        booking.setBookingItems(bookingItems);

        int total = 0;
        for(BookingItem bookingItem : bookingItems) {
            for(Ticket ticket : bookingItem.getTickets()) {
                total += ticket.getPrice();
            }
        }
        booking.setTotalPrice(total);

        return booking;
    }

    private BookingItem generateBookingItem(Booking booking) {
        List<TicketType> types = ticketTypes.get(booking.getEventId());
        TicketType tType = types.get(new Random().nextInt(types.size()));

        BookingItem bookingItem = new BookingItem();
        bookingItem.setBooking(booking);
        bookingItem.setTicketType(tType);
        bookingItem.setTicketTypeId(tType.getId());

        int quantity = randomInt.get(new Random().nextInt(randomInt.size()));
        bookingItem.setQuantity(quantity);

        List<Ticket> tickets = new LinkedList<>();
        for(int i = 0; i < quantity; i++) {
            tickets.add(generateTicket(bookingItem));
        }
        bookingItem.setTickets(tickets);

        return bookingItem;
    }

    private Ticket generateTicket(BookingItem bookingItem) {
        Ticket ticket = new Ticket();
        ticket.setStatus(ticketStatus.get(new Random().nextInt(ticketStatus.size())));
        ticket.setBookingItem(bookingItem);
        ticket.setPrice(bookingItem.getTicketType().getPrice());
        ticket.setTaxRate(bookingItem.getTicketType().getTaxRate());
        ticket.setPublicId(generate());
        return ticket;
    }

    private Customer generateCustomer(Booking booking) {
        Customer customer = new Customer();
        customer.setBookings(List.of(booking));
        customer.setEmail(firstNames.get(new Random().nextInt(firstNames.size())).toLowerCase() + "." + lastNames.get(new Random().nextInt(lastNames.size())).toLowerCase() + "@" + mailEnding.get(new Random().nextInt(mailEnding.size())));
        customer.setStripeId(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
        return customer;
    }

    private Map<String, List<TicketType>> getTicketTypes() {
        Map<String, List<TicketType>> ticketTypes = new HashMap<>();
        {
            List<TicketType> types = new LinkedList<>();
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("223f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(2500);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("b23f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(3000);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            ticketTypes.put("383f700f-5449-4e40-b509-bee0b5d139d6", types);
        }
        {
            List<TicketType> types = new LinkedList<>();
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("c23f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(1500);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("d23f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(3000);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("d53f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(3000);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            {
                TicketType ticketType = new TicketType();
                ticketType.setId("e23f700f-5449-4e40-b509-bee0b5d139d6");
                ticketType.setPrice(5000);
                ticketType.setTaxRate(7);
                types.add(ticketType);
            }
            ticketTypes.put("aa34700f-5449-4e40-b509-bee0b5d139d6", types);
        }
        return ticketTypes;
    }

    private String generate() {
        StringBuilder result = new StringBuilder();
        String alphabet = "0123456789";
        Random random = new Random();
        for(int i = 0; i < 12; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return result.toString();
    }
}
