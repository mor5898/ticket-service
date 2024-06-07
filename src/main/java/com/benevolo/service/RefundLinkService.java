package com.benevolo.service;

import com.benevolo.repo.RefundLinkRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RefundLinkService {

    @Inject
    RefundLinkRepo refundLinkRepo;

    public String findIdByBookingId(String bookingId) {
        return refundLinkRepo.findByBookingId(bookingId).getId();
    }

}
