package com.benevolo.mapper;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;

import java.util.List;

public class TicketMapper {

    public static List<TicketDTO> map(List<TicketEntity> tickets) {
        return tickets.stream().map(TicketMapper::map).toList();
    }

    public static TicketDTO map(TicketEntity ticket) {
        return new TicketDTO(ticket.getId(), ticket.getPrice(), ticket.getTaxRate(), ticket.getStatus(), CustomerMapper.map(ticket.getCustomer()));
    }

}
