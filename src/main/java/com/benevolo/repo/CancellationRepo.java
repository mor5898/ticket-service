package com.benevolo.repo;

import com.benevolo.entity.Cancellation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CancellationRepo implements PanacheRepositoryBase<Cancellation, String> {

    public List<Cancellation> findAllByEventId(String eventId) {
        return find("booking.eventId = ?1", eventId).list();
    }
}
