package com.benevolo.repo;

import com.benevolo.entity.Ticket;
import com.benevolo.utils.TicketStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TicketRepo implements PanacheRepositoryBase<Ticket, String> {

    public List<Ticket> findByEventId(String eventId, Integer pageIndex, Integer pageSize) {
        return find("SELECT t FROM Ticket AS t, BookingItem AS bi, Booking AS b WHERE t.bookingItem = bi AND bi.booking = b AND b.eventId = :eventId", Parameters.with("eventId", eventId)).page(pageIndex, pageSize).list();
    }

    public long countByEventId(String eventId) {
        return count("SELECT COUNT(t) FROM Ticket AS t, BookingItem AS bi, Booking AS b WHERE t.bookingItem = bi AND bi.booking = b AND b.eventId = :eventId", Parameters.with("eventId", eventId));
    }

    public long countByDate(String eventId, LocalDate date) {
        return count("SELECT COUNT(*) FROM Ticket AS t, BookingItem AS bi, Booking AS b WHERE t.bookingItem = bi AND bi.booking = b AND b.eventId = :eventId AND DATE(b.bookedAt) = :date",
                Parameters.with("eventId", eventId).and("date", date));
    }

}
