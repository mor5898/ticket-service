package com.benevolo.entity;

import com.benevolo.utils.TicketStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class TicketEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private int price;

    @Column(name = "tax_rate")
    private int taxRate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Column(name = "ticket_type_id")
    private String ticketTypeId;

    @Column(name = "event_id")
    private String eventId;

    public TicketEntity() {
    }

    public TicketEntity(TicketStatus status, int price, int taxRate, CustomerEntity customer, String ticketTypeId, String eventId) {
        this.id = UUID.randomUUID().toString();
        this.status = status;
        this.price = price;
        this.taxRate = taxRate;
        this.customer = customer;
        this.ticketTypeId = ticketTypeId;
        this.eventId = eventId;
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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(String ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
