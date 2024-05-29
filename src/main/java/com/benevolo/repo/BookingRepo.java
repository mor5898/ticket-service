package com.benevolo.repo;

import com.benevolo.entity.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BookingRepo implements PanacheRepositoryBase<Booking, String> {

    public long countByDate(String eventId, LocalDate date) {
        return count("SELECT COUNT(b) FROM Booking AS b WHERE b.eventId = :eventId AND DATE(b.bookedAt) = :date",
                Parameters.with("eventId", eventId).and("date", date));
    }

    public List<Booking> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        return find("eventId", eventId).page(pageIndex, pageSize).list();
    }

    public long countPagesByEventId(String eventId, Integer pageSize) {
        return (long) Math.ceil((count("eventId", eventId) * 1.0) / pageSize);
    }

    public Long findPriceByEventId(String eventId) {
        return getEntityManager().createQuery("SELECT SUM(b.totalPrice) FROM Booking AS b WHERE b.eventId = :eventId", Long.class).setParameter("eventId", eventId).getSingleResult();
    }

    public Long findAveragePriceByEventId(String eventId) {
        return Math.round(getEntityManager().createQuery("SELECT AVG(b.totalPrice) FROM Booking AS b WHERE b.eventId = :eventId", Double.class).setParameter("eventId", eventId).getSingleResult());
    }

    public Long countByEventId(String eventId) {
        return count("eventId = :eventId", Parameters.with("eventId", eventId));
    }
}
