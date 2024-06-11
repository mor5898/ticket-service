package com.benevolo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "refund_link")
public class RefundLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "refundLink")
    private List<Booking> bookings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
