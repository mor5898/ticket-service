package com.benevolo.repo;

import com.benevolo.entity.TicketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketRepo implements PanacheRepository<TicketEntity> {

}
