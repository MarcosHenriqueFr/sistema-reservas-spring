package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.dto.BookingPostDTO;
import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.model.BookingStatus;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.model.RestaurantTableStatus;
import com.example.bookingrestaurant.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    // TODO - Especificar as Exceptions de Create e find, somente Exceptions temporárias

    public Booking createBooking(BookingPostDTO newBooking, String subject) throws  Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        RestaurantTable table = restaurantTableService.findTableById(newBooking.tableId());

        // Checa as validações principais
        Booking booking = new Booking();

        restaurantTableService.checkTableValidation(table);

        boolean validTime = this.checkValidBookingDate(newBooking.date());
        if(!validTime){
            throw new Exception("Data Inválida");
        }

        booking.setUser(userAuthenticated.getUser());
        booking.setTable(table);
        booking.setBookingDate(newBooking.date());
        booking.setBookingStatus(BookingStatus.ACTIVE);

        table.setStatus(RestaurantTableStatus.BOOKED);

        saveBooking(booking);

        return booking;
    }

    private boolean checkValidBookingDate(LocalDateTime userDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime supportedDate = now.plusMonths(1);

        return userDate.isAfter(now) && !userDate.isAfter(supportedDate);
    }

    private void saveBooking(Booking booking){
        bookingRepository.save(booking);
    }

    private Booking findBookingById(Long id) throws Exception {
        return bookingRepository.findBookingById(id)
                .orElseThrow(() -> new Exception("Reserva não encontrada"));
    }

    public List<Booking> getBookingFromUser(String subject) throws Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        return bookingRepository.findByUser(userAuthenticated.getUser());
    }

    public Booking softDeleteBooking(Long id, String subject) throws Exception {
        Booking booking = this.findBookingById(id);

        List<Booking> userBookings = this.getBookingFromUser(subject);

        if(userBookings.isEmpty()){
            throw new Exception("O usuário não fez nenhuma reserva");
        }

        if(!userBookings.contains(booking)){
            throw new Exception("Reserva não encontrada");
        }

        RestaurantTable table = booking.getTable();
        table.setStatus(RestaurantTableStatus.AVAILABLE);
        booking.setBookingStatus(BookingStatus.CANCELED);

        this.saveBooking(booking);

        return booking;
    }
}
