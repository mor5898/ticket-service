package com.benevolo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class CustomerEntity extends PanacheEntityBase {

    @Id
    private String id;

    @Column(name = "stripe_id")
    private String stripeId;

    private String email;

    @OneToMany(mappedBy = "customer")
    private List<TicketEntity> tickets;

    public CustomerEntity() {
    }

    public CustomerEntity(String stripeId, String email) {
        this.id = UUID.randomUUID().toString();
        this.stripeId = stripeId;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }
}
