package com.benevolo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "booking_item")
public class BookingItem extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int quantity;

    @JsonBackReference(value="booking-item")
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @JsonIgnore
    @OneToMany(mappedBy = "bookingItem", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @Column(name = "ticket_type_id")
    private String ticketTypeId;

    @Transient
    private TicketType ticketType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(String ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        ticket.setBookingItem(this);
        this.tickets.add(ticket);
    }
}
