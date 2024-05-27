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
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class PdfService {

    private final QrCodeService qrCodeService;

    private final BookingRepo bookingRepo;

    @RestClient
    TicketTypeClient ticketTypeClient;

    @Inject
    public PdfService(QrCodeService qrCodeService, BookingRepo bookingRepo) {
        this.qrCodeService = qrCodeService;
        this.bookingRepo = bookingRepo;
    }

    public PDDocument createPdf(String bookingId) throws SQLException, WriterException, IOException {
            PDDocument document = new PDDocument();
            String ticketTypeName = "";
            String validFrom = "";
            String validTo = "";
            String eventName = "";
            String location = "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            PDType0Font font = PDType0Font.load(document, new File("font/Helvetica-Bold-Font.ttf"));
            PDImageXObject image = PDImageXObject.createFromFile("img/csm_limestone-festival-2020-1_9610915071.jpg", document);

            Booking booking = bookingRepo.findById(bookingId);
            List<BookingItem> bookingItemList = booking.getBookingItems();

            for (BookingItem item : bookingItemList) {
                String ticketTypeId = item.getTicketTypeId();
                TicketType ticketType = ticketTypeClient.findById(ticketTypeId);
                ticketTypeName = ticketType.getName();
                validFrom = formatter.format(ticketType.getValidFrom());
                validTo = formatter.format(ticketType.getValidTo());
                eventName = ticketType.getEvent().getEventName();
                location = ticketType.getEvent().getAddress().getCity();

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
                    PDImageXObject qrCodeImage = PDImageXObject.createFromByteArray(document, qrCodeService.generateQRCode(ticketId), "qrCode");
                    float startY = 750 - (count * 250);

                    drawTicket(document,
                            page,
                            font,
                            image,
                            startY,
                            qrCodeImage,
                            eventName,
                            ticket.getPrice(),
                            ticket.getId(),
                            ticketTypeName,
                            validFrom,
                            location,
                            validTo);
                    count++;

                }
            }

            //document.save("ticketExample\\FestivalTickets.pdf"); // only for testing, needs to be removed for production
            return document;
    }

    private static void drawTicket(PDDocument document, PDPage page, PDType0Font font, PDImageXObject image, float startY, PDImageXObject qrCode,
                                   String eventName, int price, String ticketId, String ticketTypeName, String validFrom, String location, String validTo) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {

            // Hintergrundbild für das Ticket
            PDImageXObject bgImage = PDImageXObject.createFromFile("img/AdobeStock_456815540_Preview.jpeg", document);
            contentStream.drawImage(bgImage, 0, startY - 240, PDRectangle.A4.getWidth(), 265);

            // QR-Code
            contentStream.drawImage(qrCode, 500, startY - 80, 100, 100);

            //TicketId
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(350, startY - 200);
            contentStream.showText(ticketId);
            contentStream.endText();

            // Festivalname links oben
            contentStream.beginText();
            contentStream.setFont(font, 22);
            contentStream.newLineAtOffset(27, startY);
            contentStream.showText(eventName);
            contentStream.endText();

            // Ort des Festivals
            contentStream.beginText();
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(27, startY - 20);
            contentStream.showText(location);
            contentStream.endText();

            // Preis des Tickets
            contentStream.beginText();
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(27, startY - 60);
            contentStream.showText(String.valueOf(price) + " EUR");
            contentStream.endText();

            // Tickettyp
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(27, startY - 80);
            contentStream.showText(ticketTypeName);
            contentStream.endText();

            // Gülitigkeitsdatum Ticket
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(27, startY - 100);
            contentStream.showText(validFrom + " - " + validTo);
            contentStream.endText();
        }
    }
}