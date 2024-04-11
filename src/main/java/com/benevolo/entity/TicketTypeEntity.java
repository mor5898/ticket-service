package com.benevolo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_type")
public class TicketTypeEntity {

    @Id
    private String id;
    @Column(name = "ticket_type_name")
    private String name;


}
