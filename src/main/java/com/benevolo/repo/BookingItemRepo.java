package com.benevolo.repo;

import com.benevolo.entity.BookingItem;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingItemRepo implements PanacheRepositoryBase<BookingItem, String> {
}
