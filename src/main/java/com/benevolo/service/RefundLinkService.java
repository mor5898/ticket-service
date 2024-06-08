package com.benevolo.service;

import com.benevolo.repo.BookingRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RefundLinkService {

    @Inject
    BookingRepo bookingRepo;

    public String findIdByBookingId(String bookingId) {
        return bookingRepo.findById(bookingId).getRefundLink().getId();
    }

}
