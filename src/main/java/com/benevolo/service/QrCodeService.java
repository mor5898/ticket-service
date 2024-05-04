package com.benevolo.service;

import com.benevolo.entity.Ticket;
import com.benevolo.repo.TicketRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QrCodeService {

    private final TicketRepo ticketRepo;

    @Inject
    public QrCodeService(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public byte[] generateQRCode(String ticketId) throws SQLException, WriterException, IOException {
        Ticket ticket = ticketRepo.findById(ticketId);
        if (ticket != null) {
            Map<String, Object> qrData = new HashMap<>();
            qrData.put("ticket_id", ticketId);
            qrData.put("status", ticket.getStatus());
            // more data?

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(qrData);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            BitMatrix matrix = new QRCodeWriter().encode(json, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(matrix, "PNG", bas);
            return bas.toByteArray();
        } else {
            return null;
        }
    }
}
