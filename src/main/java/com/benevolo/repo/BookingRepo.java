package com.benevolo.repo;

import com.benevolo.entity.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class BookingRepo implements PanacheRepositoryBase<Booking, String> {

    public long countByDate(String eventId, LocalDate date) {
        return count("SELECT COUNT(b) FROM Booking AS b WHERE b.eventId = :eventId AND DATE(b.bookedAt) = :date",
                Parameters.with("eventId", eventId).and("date", date));
    }
}
