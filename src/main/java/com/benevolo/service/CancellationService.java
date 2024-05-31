package com.benevolo.service;

import com.benevolo.entity.Cancellation;
import com.benevolo.repo.CancellationRepo;
import com.benevolo.utils.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CancellationService {

    @Inject
    CancellationRepo cancellationRepo;

    @Transactional
    public Cancellation changeCancellationStatus(String cancellationId, boolean isApproved) {
        Cancellation cancellation = Cancellation.findById(cancellationId);
        if (cancellation == null) {
            throw new NotFoundException("Cancellation with ID " + cancellationId + " not found");
        }
        if (isApproved) {
            cancellation.setStatus(TicketStatus.REDEEMED);
        } else {
            cancellation.setStatus(TicketStatus.CANCELLED);
        }
        cancellation.persist();
        return cancellation;
    }

    @Transactional
    public Cancellation save(Cancellation cancellation) {
        cancellation.setRequestedAt(LocalDateTime.now());
        cancellation.setStatus(TicketStatus.PENDING);
        cancellation.persist();
        return cancellation;
    }

    public List<Cancellation> getAllCancellations() {
        return cancellationRepo.listAll().stream().toList();
    }
}
