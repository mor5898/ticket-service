package com.benevolo.entity;

import com.benevolo.utils.TicketStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Cancellation extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;


    public void setId(String id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Booking getBooking() {
        return booking;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
