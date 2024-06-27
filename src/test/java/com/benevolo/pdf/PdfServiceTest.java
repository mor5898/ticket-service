package com.benevolo.pdf;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.*;
import com.benevolo.repo.BookingRepo;
import com.benevolo.service.PdfService;
import com.benevolo.service.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
public class PdfServiceTest {

    @Mock
    QrCodeService qrCodeService;

    @Mock
    BookingRepo bookingRepo;

    @Mock
    @RestClient
    TicketTypeClient ticketTypeClient;

    @InjectMocks
    PdfService pdfService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePdf() throws WriterException, IOException {
        String bookingId = "test-booking-id";
        String ticketId = "test-ticket-id";
        String ticketTypeId = "test-ticket-type-id";

        Booking booking = new Booking();
        booking.setId(bookingId);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setTicketTypeId(ticketTypeId);
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setPrice(123);
        bookingItem.setTickets(Collections.singletonList(ticket));
        booking.setBookingItems(Collections.singletonList(bookingItem));

        Event event = new Event("event_id_1", "Event 1", new Address("Street", "City", "State", "Zip"), false);
        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setName("Test Ticket Type");
        ticketType.setEvent(event);
        ticketType.setValidFrom(LocalDateTime.now());
        ticketType.setValidTo(LocalDateTime.now().plusDays(1));

        when(bookingRepo.findById(anyString())).thenReturn(booking);
        when(ticketTypeClient.findById(anyString())).thenReturn(ticketType);

        // Mock QR code generation
        ByteArrayOutputStream mockQrCodeStream = new ByteArrayOutputStream();
        byte[] mockImageData = generateMockQRCodeImageBytes(ticketId);
        mockQrCodeStream.write(mockImageData);
        when(qrCodeService.generateQRCode(anyString())).thenReturn(mockQrCodeStream);

        PDDocument document = pdfService.createPdf(bookingId);

        assertNotNull(document, "PDF document should not be null");
        assertNotNull(document.getPage(0), "PDF should have at least one page");

        document.close();
    }

    private byte[] generateMockQRCodeImageBytes(String ticketId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BitMatrix matrix = new QRCodeWriter().encode(ticketId, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
