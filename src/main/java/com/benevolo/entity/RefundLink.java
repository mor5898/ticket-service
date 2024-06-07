package com.benevolo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "refund_link")
public class RefundLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "booking_id")
    @OneToOne
    private Booking booking;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
