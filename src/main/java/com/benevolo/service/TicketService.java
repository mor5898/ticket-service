package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.EventRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import java.util.List;

@ApplicationScoped
public class TicketService {

    private final TicketRepo ticketRepo;
    private final EventRepo eventRepo;

    @Inject
    public TicketService(TicketRepo ticketRepo, EventRepo eventRepo) {
        this.ticketRepo = ticketRepo;
        this.eventRepo = eventRepo;
    }

    public List<TicketDTO> findAllByEventId(String eventId) {
        return TicketMapper.map(ticketRepo.findByEvent(eventRepo.findById(eventId)));
    }

    public void updateTicket(String ticketID, TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketRepo.findByID(ticketID).orElseThrow();
        ticketEntity.setId(ticketDTO.id());
        ticketEntity.setStatus(ticketDTO.status());
        ticketEntity.setPrice(ticketDTO.price());
        ticketEntity.setTaxRate(ticketDTO.taxRate());
        ticketEntity.setCustomer(ticketDTO.customer());
        TicketRepo.save(ticketEntity);
    }

    public void saveTicket(TicketDTO ticketDTO) {
        TicketEntity ticketEntity = TicketMapper.map(ticketDTO);
        if(ticketDTO.id().isBlank()) {
            ticketEntity.setId(UUID.randomUUID().toString());
        }
        TicketRepo.save(ticketEntity);
    }
}