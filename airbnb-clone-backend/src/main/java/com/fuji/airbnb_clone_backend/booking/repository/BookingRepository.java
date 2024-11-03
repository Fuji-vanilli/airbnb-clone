package com.fuji.airbnb_clone_backend.booking.repository;

import com.fuji.airbnb_clone_backend.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
