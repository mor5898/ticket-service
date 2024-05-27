package com.benevolo.entity;

import com.benevolo.utils.TicketStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "public_id")
    private String publicId;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private int price;

    @Column(name = "tax_rate")
    private int taxRate;

    @ManyToOne
    @JoinColumn(name = "booking_item_id")
    private BookingItem bookingItem;

    public Ticket() {
        // empty constructor
    }

    public Ticket(TicketStatus status, int price, int taxRate) {
        this.publicId = generate();
        this.status = status;
        this.price = price;
        this.taxRate = taxRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public BookingItem getBookingItem() {
        return bookingItem;
    }

    public void setBookingItem(BookingItem bookingItem) {
        this.bookingItem = bookingItem;
    }

    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        String alphabet = "0123456789";
        for(int i = 0; i < 6; i++) {
            stringBuilder.append(alphabet.charAt(new Random().nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }
}
