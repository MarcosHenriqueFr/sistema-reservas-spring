package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.dto.BookingPostDTO;
import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    // TODO - Especificar as Exceptions de Create e find

    public Booking createBooking(BookingPostDTO newBooking, String subject) throws  Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        RestaurantTable table = restaurantTableService.findTableById(newBooking.tableId());
        Booking booking = new Booking();

        // TODO Validação de datas
        booking.setUser(userAuthenticated.getUser());
        booking.setTable(table);
        booking.setBooking_date(newBooking.date());

        saveBooking(booking);

        return booking;
    }

    private void saveBooking(Booking booking){
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingFromUser(String subject) throws Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        return bookingRepository.findByUser(userAuthenticated.getUser());
    }
}
