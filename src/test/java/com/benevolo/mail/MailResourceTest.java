package com.benevolo.mail;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Customer;
import com.benevolo.repo.BookingRepo;
import com.benevolo.rest.MailResource;
import com.benevolo.service.MailService;
import com.benevolo.service.PdfService;
import com.benevolo.utils.EmailBuilder;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MailResourceTest {

    @Mock
    PdfService pdfService;

    @Mock
    MailService mailService;

    @Mock
    BookingRepo bookingRepo;

    @InjectMocks
    MailResource mailResource;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSend() {
        EmailBuilder emailBuilder = new EmailBuilder();
        mailResource.send(emailBuilder);

        verify(mailService, times(1)).send(emailBuilder);
    }

    @Test
    void testSendEmailWithPdf() throws Exception {
        String bookingId = "test-booking-id";
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setBookingId(bookingId);

        Booking booking = new Booking();
        booking.setId(bookingId);

        byte[] mockPdfContent = new byte[] {1, 2, 3};
        when(pdfService.createPdf(anyString())).thenReturn(mockPdfContent);
        doNothing().when(mailService).sendEmailWithPdf(emailBuilder, mockPdfContent, booking);
        when(bookingRepo.findById(anyString())).thenReturn(booking);

        mailResource.sendEmailWithPdf(emailBuilder);

        verify(bookingRepo, times(1)).findById(bookingId);
        verify(pdfService, times(1)).createPdf(bookingId);
        verify(mailService, times(1)).sendEmailWithPdf(emailBuilder, mockPdfContent, booking);
    }

    @Test
    void testSendEmailWithPdf_ExceptionHandling() throws Exception {
        String bookingId = "test-booking-id";
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setBookingId(bookingId);

        Booking booking = new Booking();
        booking.setId(bookingId);

        when(bookingRepo.findById(anyString())).thenReturn(booking);
        when(pdfService.createPdf(anyString())).thenThrow(new RuntimeException("Mocked exception"));

        WebApplicationException exception = org.junit.jupiter.api.Assertions.assertThrows(WebApplicationException.class, () -> {
            mailResource.sendEmailWithPdf(emailBuilder);
        });

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getResponse().getStatus());
        verify(bookingRepo, times(1)).findById(bookingId);
        verify(pdfService, times(1)).createPdf(bookingId);
        verify(mailService, never()).sendEmailWithPdf(any(), any(), any());
    }

    @Test
    void testSendEventReminder() {
        String eventId = "test-event-id";
        Booking booking = new Booking();
        booking.setId("test-booking-id");
        booking.setEventId("test-event-id");
        Customer customer = new Customer("stripeId-567", "customer@test.com");
        booking.setCustomer(customer);

        List<Booking> bookings = Collections.singletonList(booking);

        PanacheQuery<Booking> mockQuery = mock(PanacheQuery.class);
        when(bookingRepo.find(anyString(), any(Parameters.class))).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(bookings);

        mailResource.sendEventReminder(eventId);

        verify(bookingRepo, times(1)).find(eq("eventId = :eventId"), any(Parameters.class));
        verify(mailService, times(1)).send(any(EmailBuilder.class));
    }
}
