package com.benevolo.dto;

import com.benevolo.utils.TicketStatus;

public record TicketDTO(String id, int price, int taxRate, TicketStatus status, CustomerDTO customer) {
}
