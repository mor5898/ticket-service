package com.benevolo.service;

import com.benevolo.entity.Booking;
import com.benevolo.entity.Cancellation;
import com.benevolo.entity.Ticket;
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
    public Cancellation save(Cancellation cancellation, String bookingId) {
        Ticket ticket = Ticket.findById(cancellation.getTicket().getId());
        Booking booking = Booking.findById(bookingId);
        if (booking != null && ticket != null) {
            cancellation.setTicket(ticket);
            cancellation.setBooking(booking);
            cancellation.setRequestedAt(LocalDateTime.now());
            cancellation.setStatus(TicketStatus.PENDING);
            cancellation.persist();
            return cancellation;
        } else {
            throw new RuntimeException("The supplied ticketId or bookingId does not exist!");
        }
    }

    public List<Cancellation> getAllCancellations() {
        return cancellationRepo.listAll().stream().toList();
    }
}
