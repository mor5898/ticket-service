package com.benevolo.service;

import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import com.benevolo.mapper.TicketMapper;
import com.benevolo.repo.TicketRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

}
