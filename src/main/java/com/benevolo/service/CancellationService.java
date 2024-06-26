package com.benevolo.service;

import com.benevolo.entity.Cancellation;
import com.benevolo.repo.CancellationRepo;
import com.benevolo.repo.RefundLinkRepo;
import com.benevolo.repo.TicketRepo;
import com.benevolo.utils.CancellationStatus;
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

    @Inject
    RefundLinkRepo refundLinkRepo;

    @Inject
    TicketRepo ticketRepo;

    @Transactional
    public Cancellation changeCancellationStatus(String cancellationId, boolean isApproved) {
        Cancellation cancellation = cancellationRepo.findById(cancellationId);
        if (cancellation == null) {
            throw new NotFoundException("Cancellation with ID " + cancellationId + " not found");
        }
        if (isApproved) {
            cancellation.setStatus(CancellationStatus.ACCEPTED);
        } else {
            cancellation.setStatus(CancellationStatus.DECLINED);
        }
        cancellation.persist();
        return cancellation;
    }

    @Transactional
    public void save(List<String> ticketIds, String refundId) {
        ticketIds.forEach(ticket -> cancelTicket(ticket, refundId));
    }

    private void cancelTicket(String id, String refundId) {
        Cancellation cancellation = new Cancellation();
        cancellation.setBooking(refundLinkRepo.findById(refundId).getBookings().getFirst());
        cancellation.setRequestedAt(LocalDateTime.now());
        cancellation.setTicket(ticketRepo.findById(id));
        cancellation.setStatus(CancellationStatus.PENDING);
        cancellation.persist();
    }

    public List<Cancellation> getAllCancellations() {
        return cancellationRepo.listAll().stream().toList();
    }
}
