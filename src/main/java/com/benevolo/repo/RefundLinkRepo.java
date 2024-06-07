package com.benevolo.repo;

import com.benevolo.entity.RefundLink;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefundLinkRepo implements PanacheRepositoryBase<RefundLink, String> {

    public RefundLink findByBookingId(String bookingId) {
        return find("booking.id = :bookingId", Parameters.with("bookingId", bookingId)).firstResult();
    }

}
