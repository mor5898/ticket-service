package com.benevolo.repo;

import com.benevolo.entity.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingRepo implements PanacheRepositoryBase<Booking, String> {
}
