package com.benevolo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "event")
public class EventEntity extends PanacheEntityBase {

    @Id
    private String id;
    @OneToMany(mappedBy = "event")
    List<TicketTypeEntity> ticketTypes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TicketTypeEntity> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeEntity> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
