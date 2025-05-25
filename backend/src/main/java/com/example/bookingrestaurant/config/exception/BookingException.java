package com.example.bookingrestaurant.config.exception;

/**
 * Exception checada responsável por qualquer exceção relacionada às reservas.
 */
public class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}
