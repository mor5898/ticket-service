package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.TicketStatus
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import java.util.List;

@ApplicationScoped
public class TicketService {

    private final TicketRepo ticketRepo;

    @Inject
    public TicketService(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public List<TicketDTO> findAll() {
        return TicketMapper.map(ticketRepo.findAll().stream().toList());
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