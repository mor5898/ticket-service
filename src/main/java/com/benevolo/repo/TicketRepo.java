package com.benevolo.repo;

import com.benevolo.entity.EventEntity;
import com.benevolo.entity.TicketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class TicketRepo implements PanacheRepositoryBase<TicketEntity, String> {

    public List<TicketEntity> findByEvent(EventEntity event) {
        return list("SELECT t FROM TicketEntity AS t, TicketTypeEntity AS tt WHERE t.ticketType = tt AND tt.event = :event", Parameters.with("event", event));
    }

}
