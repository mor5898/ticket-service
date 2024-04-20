package com.benevolo.dto;

public record BookingDTO(int quantity,
                         String eventId,
                         String ticketTypeId,
                         CustomerDTO customer) { }
