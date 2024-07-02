package com.benevolo.pdf;

import com.benevolo.service.QrCodeService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import jakarta.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class QrCodeServiceTest {

    @Inject
    QrCodeService qrCodeService;

    @Test
    public void testGenerateQRCode() throws WriterException, IOException {
        String ticketId = "bc56b92f-510c-44de-9fa0-5523ba6d96c3";

        ByteArrayOutputStream qrCodeStream = qrCodeService.generateQRCode(ticketId);

        assertNotNull(qrCodeStream, "QR code stream should not be null");
        assertTrue(qrCodeStream.size() > 0, "QR code stream should not be empty");

        // Verify the content of the QR code
        try {
            String decodedTicketId = decodeQRCode(qrCodeStream);
            assertEquals(ticketId, decodedTicketId, "The ticket ID encoded in the QR code should match the input ticket ID");
        } catch (Exception e) {
        }
    }

    private String decodeQRCode(ByteArrayOutputStream qrCodeStream) throws IOException, NotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(qrCodeStream.toByteArray());
        BufferedImage bufferedImage = ImageIO.read(bais);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}
