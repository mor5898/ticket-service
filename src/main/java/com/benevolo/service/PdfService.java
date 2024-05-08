package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.BookingItem;
import com.benevolo.entity.Ticket;
import com.benevolo.entity.TicketType;
import com.benevolo.repo.BookingRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class PdfService {

    private final QrCodeService qrCodeService;

    private final BookingRepo bookingRepo;
    @Inject
    TicketService ticketService;
    @Inject
    public PdfService(QrCodeService qrCodeService, BookingRepo bookingRepo) {
        this.qrCodeService = qrCodeService;
        this.bookingRepo = bookingRepo;
    }

    public PDDocument createPdf(String eventNameP, String eventId, String bookingId) throws SQLException, WriterException {
        try (PDDocument document = new PDDocument()) {

            String ticketType = "";
            String validFrom ="";
            String eventName ="";
            String location ="";

            PDType0Font font = PDType0Font.load(document, new File("font/Helvetica-Bold-Font.ttf"));
            PDImageXObject image = PDImageXObject.createFromFile("img/csm_limestone-festival-2020-1_9610915071.jpg", document);

            Booking booking = bookingRepo.findById(bookingId);
            List<BookingItem> bookingItemList = booking.getBookingItems();

            for (BookingItem item : bookingItemList) {
                try {
                    String ticketTypeId = item.getTicketTypeId();
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/api/event-service/ticket-types/" + ticketTypeId))
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode requestData = mapper.readTree(response.body());

                    ticketType = requestData.get("name").asText();
                    validFrom = requestData.get("validFrom").asText();
                    eventName = requestData.path("event").path("eventName").asText();
                    location = requestData.path("event").path("address").path("city").asText();
                    //System.out.println("API Response: " + response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int count = 0;
                List<Ticket> tickets = item.getTickets();
                PDPage page = new PDPage();
                document.addPage(page);
                //TicketType ticketType = item.getTicketType();
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
                           eventId,
                           ticket.getPrice(),
                           ticket.getId(),
                           ticketType,
                            validFrom,
                            location);
                            count++;

                }
            }

            document.save("ticketExample\\FestivalTickets.pdf"); // necessary?
            return document;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void drawTicket(PDDocument document, PDPage page, PDType0Font font, PDImageXObject image, float startY, PDImageXObject qrCode,
                                   String eventName, String eventId, int price, String ticketId, String ticketTypeName, String validFrom, String location) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {

            // Hintergrundbild für das Ticket
            PDImageXObject bgImage = PDImageXObject.createFromFile("img/AdobeStock_456815540_Preview.jpeg", document);
            contentStream.drawImage(bgImage, 0, startY - 240, PDRectangle.A4.getWidth(), 265);

            //PDImageXObject bgImage = PDImageXObject.createFromFile("img/_6de14522-d0db-464a-840b-853726a9bc7d.jpeg", document);
            //contentStream.drawImage(bgImage, 0, startY, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()/3 );

            // QR-Code
            contentStream.drawImage(qrCode, 500, startY - 80, 100, 100);

            //FestivalId
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
            contentStream.newLineAtOffset(27, startY -60);
            contentStream.showText(String.valueOf(price) + " EUR");
            contentStream.endText();

            // Tickettyp
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(27, startY - 80);
            contentStream.showText(ticketTypeName);
            contentStream.endText();

            // EventStart
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(27, startY - 100);
            contentStream.showText(validFrom);
            contentStream.endText();

        }
    }
}