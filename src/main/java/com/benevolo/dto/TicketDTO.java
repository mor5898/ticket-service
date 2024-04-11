package com.benevolo.dto;

import com.benevolo.utils.TicketStatus;

public record TicketDTO(String id, TicketStatus status, CustomerDTO customer) {
}
