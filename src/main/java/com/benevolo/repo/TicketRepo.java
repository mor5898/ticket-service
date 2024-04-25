package com.benevolo.repo;

import com.benevolo.entity.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TicketRepo implements PanacheRepositoryBase<Ticket, String> {

    public List<Ticket> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        return find("SELECT t FROM Ticket AS t, BookingItem AS bi, Booking AS b WHERE t.bookingItem = bi AND bi.booking = b AND b.eventId = :eventId", Parameters.with("eventId", eventId)).page(pageIndex, pageSize).list();
    }

    public long countByEventId(String eventId) {
        return count("eventId" ,eventId);
    }

}
