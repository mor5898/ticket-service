package com.benevolo.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ApplicationScoped
public class QrCodeService {

    public ByteArrayOutputStream generateQRCode(String ticketId) throws WriterException, IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        BitMatrix matrix = new QRCodeWriter().encode(ticketId, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(matrix, "PNG", bas);
        return bas;
    }
}
