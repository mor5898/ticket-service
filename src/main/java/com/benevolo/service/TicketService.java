package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.EventRepo;
import com.benevolo.repo.TicketRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
}
