package com.benevolo.service;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.dto.BookingDTO;
import com.benevolo.dto.TicketDTO;
import com.benevolo.dto.TicketTypeDTO;
import com.benevolo.entity.CustomerEntity;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class TicketService {

    @RestClient
    TicketTypeClient ticketTypeClient;

    private final TicketRepo ticketRepo;

    private final TicketMapper ticketMapper;

    private final JsonWebToken jsonWebToken;

    @Inject
    public TicketService(TicketRepo ticketRepo, TicketMapper ticketMapper, JsonWebToken jsonWebToken) {
        this.ticketRepo = ticketRepo;
        this.ticketMapper = ticketMapper;
        this.jsonWebToken = jsonWebToken;
    }

    public List<TicketDTO> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        return ticketMapper.map(ticketRepo.findByEventId(eventId, pageIndex, pageSize));
    }

    public long countByEventId(String eventId, Integer pageSize) {
        return ticketRepo.countByEventId(eventId) / pageSize + 1;
    }

    public void update(String ticketId, TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketRepo.findById(ticketId);
        ticketEntity.setStatus(ticketDTO.status());
        ticketEntity.setPrice(ticketDTO.price());
        ticketEntity.setTaxRate(ticketDTO.taxRate());
        ticketRepo.persist(ticketEntity);
    }

    @Transactional
    public void save(List<BookingDTO> bookings) {
        final List<TicketEntity> tickets = new LinkedList<>();
        for(BookingDTO booking : bookings) {
            for(int i = 0; i < booking.quantity(); i++) {
                tickets.add(generateTicket(booking));
            }
        }
        ticketRepo.persist(tickets);
    }

    private TicketEntity generateTicket(BookingDTO booking) {
        TicketTypeDTO ticketTypeDTO = ticketTypeClient.findById("Bearer " + jsonWebToken.getRawToken(), booking.ticketTypeId());
        return new TicketEntity(TicketStatus.PENDING, ticketTypeDTO.price(), ticketTypeDTO.taxRate(), new CustomerEntity(booking.customer().stripeId(), booking.customer().email()), ticketTypeDTO.id(), booking.eventId());
    }
}