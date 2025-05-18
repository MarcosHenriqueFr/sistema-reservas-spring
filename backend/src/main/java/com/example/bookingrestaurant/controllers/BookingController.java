package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.dto.BookingPostDTO;
import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "reservas")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingPostDTO data, @AuthenticationPrincipal Jwt jwt) throws Exception {
        Booking booking = bookingService.createBooking(data, jwt.getSubject());
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookingFromUser(@AuthenticationPrincipal Jwt jwt) throws Exception {
        List<Booking> bookings = bookingService.getBookingFromUser(jwt.getSubject());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
