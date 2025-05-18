package com.example.bookingrestaurant.repositories;

import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingById(Long id);
    List<Booking> findByUser(User user);
}
