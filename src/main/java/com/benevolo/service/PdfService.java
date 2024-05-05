package com.benevolo.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

@ApplicationScoped
public class PdfService {

    public PDDocument createPdf(byte[] qrCode) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Laden Sie die Schriftart und das Bild
            PDType0Font font = PDType0Font.load(document, new File("font/Helvetica-Bold-Font.ttf"));
            PDImageXObject image = PDImageXObject.createFromFile("img/csm_limestone-festival-2020-1_9610915071.jpg", document);
            PDImageXObject qrCodeImage = PDImageXObject.createFromByteArray(document, qrCode, "qrCode");

            // Erstellen Sie drei Tickets auf der Seite
            for (int i = 0; i < 3; i++) {
                float startY = 750 - (i * 250);
                drawTicket(document, page, font, image, startY, qrCodeImage);
            }

            document.save("ticketExample\\FestivalTickets.pdf"); // necessary?
            return document;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void drawTicket(PDDocument document, PDPage page, PDType0Font font, PDImageXObject image, float startY, PDImageXObject qrCode) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            // Festivalname
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(50, startY);
            contentStream.showText("Festival Name");
            contentStream.endText();

            // QR-Code (Dummy-Wert)
            contentStream.drawImage(qrCode, 500, startY - 50, 50, 50);

            // Ticketnummer
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(500, startY - 60);
            contentStream.showText("Ticket Nr. 12345");
            contentStream.endText();

            // Bild in der Mitte des Tickets
            contentStream.drawImage(image, 200, startY - 125, 100, 100);
        }
    }
}

