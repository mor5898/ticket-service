package com.benevolo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket_type")
public class TicketTypeEntity {

    @Id
    private String id;
    @Column(name = "ticket_type_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }
}
