package com.benevolo.service;

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
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class PdfService {

    private final QrCodeService qrCodeService;

    private final BookingRepo bookingRepo;

    @Inject
    public PdfService(QrCodeService qrCodeService, BookingRepo bookingRepo) {
        this.qrCodeService = qrCodeService;
        this.bookingRepo = bookingRepo;
    }

    public PDDocument createPdf(String eventId, String eventName, String bookingId) throws SQLException, WriterException {
        try (PDDocument document = new PDDocument()) {

            PDType0Font font = PDType0Font.load(document, new File("font/Helvetica-Bold-Font.ttf"));
            PDImageXObject image = PDImageXObject.createFromFile("img/csm_limestone-festival-2020-1_9610915071.jpg", document);

            Booking booking = bookingRepo.findById(bookingId);
            List<BookingItem> bookingItemList = booking.getBookingItems();
            int count = 0;
            for (BookingItem item : bookingItemList) {
                List<Ticket> tickets = item.getTickets();
                PDPage page = new PDPage();
                document.addPage(page);
                //TicketType ticketType = item.getTicketType();
                for (Ticket ticket : tickets) {
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
                           // ticketType.getName());
                            "Blabla");
                    if (count >= 3) {
                        page = new PDPage();
                        document.addPage(page);
                        count = 0;
                    }
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
                                   String eventName, String ticketTypeName) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            // Festivalname
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(50, startY);
            contentStream.showText(eventName);
            contentStream.endText();

            // QR-Code
            contentStream.drawImage(qrCode, 500, startY - 50, 50, 50);

            // Tickettyp
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(500, startY - 60);
            contentStream.showText(ticketTypeName);
            contentStream.endText();

            // Bild in der Mitte des Tickets
            contentStream.drawImage(image, 200, startY - 125, 100, 100);
        }
    }
}