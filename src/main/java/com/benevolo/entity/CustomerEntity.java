package com.benevolo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerEntity extends PanacheEntityBase {

    @Id
    @Column(name = "stripe_id")
    private String id;
    private String email;
    @Column(name = "customer_name")
    private String name;
    @OneToMany(mappedBy = "customer")
    private List<TicketEntity> tickets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
