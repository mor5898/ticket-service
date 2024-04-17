package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.entity.TicketTypeEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.EventRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.repo.TicketTypeRepo;
import com.benevolo.utils.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TicketService {

    private final TicketRepo ticketRepo;
    private final TicketTypeRepo ticketTypeRepo;
    private final EventRepo eventRepo;

    @Inject
    public TicketService(TicketRepo ticketRepo, TicketTypeRepo ticketTypeRepo, EventRepo eventRepo) {
        this.ticketRepo = ticketRepo;
        this.ticketTypeRepo = ticketTypeRepo;
        this.eventRepo = eventRepo;
    }

    public List<TicketDTO> findAllByEventId(String eventId) {
        return TicketMapper.map(ticketRepo.findByEvent(eventRepo.findById(eventId)));
    }

    public void update(String ticketId, TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketRepo.findById(ticketId);
        ticketEntity.setStatus(ticketDTO.status());
        ticketEntity.setPrice(ticketDTO.price());
        ticketEntity.setTaxRate(ticketDTO.taxRate());
        ticketRepo.persist(ticketEntity);
    }

    public void save(String ticketTypeId) {
        TicketTypeEntity ticketType = ticketTypeRepo.findById(ticketTypeId);
        ticketRepo.persist(new TicketEntity(TicketStatus.PENDING, ticketType.getPrice(), ticketType.getTaxRate(), null, ticketType));
    }
}