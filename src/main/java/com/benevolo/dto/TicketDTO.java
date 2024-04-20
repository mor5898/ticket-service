package com.benevolo.dto;

import com.benevolo.utils.TicketStatus;
import java.time.LocalDateTime;

public record TicketDTO(String id, String publicId, int price, int taxRate, TicketStatus status, LocalDateTime bookedAt, CustomerDTO customer, TicketTypeDTO ticketType) {
}