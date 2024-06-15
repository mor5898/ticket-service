package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Ticket;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import com.google.zxing.WriterException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class PdfService {

    @Inject
    QrCodeService qrCodeService;

    @Inject
    BookingRepo bookingRepo;

    @RestClient
    TicketTypeClient ticketTypeClient;

    public PDDocument createPdf(String bookingId) throws WriterException, IOException {
        PDDocument document = new PDDocument();
        Booking booking = bookingRepo.findById(bookingId);
        List<BookingItem> bookingItemList = booking.getBookingItems();

        for (BookingItem item : bookingItemList) {
            String ticketTypeId = item.getTicketTypeId();
            TicketType ticketType = ticketTypeClient.findById(ticketTypeId);
            int count = 0;
            List<Ticket> tickets = item.getTickets();
            PDPage page = new PDPage();
            document.addPage(page);
            for (Ticket ticket : tickets) {
                if (count >= 3) {
                    page = new PDPage();
                    document.addPage(page);
                    count = 0;
                }
                String ticketId = ticket.getId();
                PDImageXObject qrCodeImage = PDImageXObject.createFromByteArray(document, qrCodeService.generateQRCode(ticketId).toByteArray(), "qrCode");
                float startY = 750 - (count * 250);
                drawTicket(document, page, startY, qrCodeImage, ticket, ticketType);
                count++;

            }
        }
        return document;
    }

    private static void drawTicket(PDDocument document, PDPage page, float startY, PDImageXObject qrCode, Ticket ticket, TicketType ticketType) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            PDType0Font font = PDType0Font.load(document, new File("font/Helvetica-Bold-Font.ttf"));
            // Hintergrundbild für das Ticket
            PDImageXObject bgImage = PDImageXObject.createFromFile("img/AdobeStock_456815540_Preview.jpeg", document);
            contentStream.drawImage(bgImage, 0, startY - 240, PDRectangle.A4.getWidth(), 265);

            // QR-Code
            contentStream.drawImage(qrCode, 500, startY - 80, 100, 100);

            //TicketId
            addText(contentStream, font, 12, 350, startY - 200, ticket.getId());

            // Festivalname links oben
            addText(contentStream, font, 22, 27, startY, ticketType.getEvent().getEventName());

            // Ort des Festivals
            addText(contentStream, font, 16, 27, startY - 20, ticketType.getEvent().getAddress().getCity());

            // Preis des Tickets
            String formattedPrice = String.format("%03d", ticket.getPrice());
            int length = formattedPrice.length();
            String euroString = formattedPrice.substring(0, length - 2) + "," + formattedPrice.substring(length - 2);
            addText(contentStream, font, 16, 27, startY - 60, euroString + " EUR");

            // Tickettyp
            addText(contentStream, font, 12, 27, startY - 80, ticketType.getName());

            // Gülitigkeitsdatum Ticket
            String validationDateFormatted = formatter.format(ticketType.getValidFrom()) + " - " + formatter.format(ticketType.getValidTo());
            addText(contentStream, font, 12, 27, startY - 100, validationDateFormatted);
        }
    }

    private static void addText(PDPageContentStream contentStream, PDType0Font font, int fontSize, float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}