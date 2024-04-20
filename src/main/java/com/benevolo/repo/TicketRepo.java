package com.benevolo.repo;

import com.benevolo.entity.TicketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TicketRepo implements PanacheRepositoryBase<TicketEntity, String> {

    public List<TicketEntity> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        return find("eventId" ,eventId).page(pageIndex, pageSize).list();
    }

    public long countByEventId(String eventId) {
        return count("eventId" ,eventId);
    }

}
