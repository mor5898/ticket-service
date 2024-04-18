package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.TicketRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public class TicketService {

    private final TicketRepo ticketRepo;
    private final TicketMapper ticketMapper;

    @Inject
    public TicketService(TicketRepo ticketRepo, TicketMapper ticketMapper) {
        this.ticketRepo = ticketRepo;
        this.ticketMapper = ticketMapper;
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

    public void save(String ticketTypeId) {
        //ticketRepo.persist(new TicketEntity(TicketStatus.PENDING, ticketType.getPrice(), ticketType.getTaxRate(), null, ticketType));
        throw new NotImplementedYet();
    }
}