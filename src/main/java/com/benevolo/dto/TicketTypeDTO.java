package com.benevolo.dto;

import java.time.LocalDateTime;

public record TicketTypeDTO(String id,
                            String name,
                            double price,
                            double taxRate,
                            int capacity,
                            boolean active,
                            LocalDateTime validFrom,
                            LocalDateTime validTo,
                            String eventId) { }