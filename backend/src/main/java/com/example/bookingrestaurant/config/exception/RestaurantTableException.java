package com.example.bookingrestaurant.config.exception;

/**
 * Exception checada responsável por qualquer exceção relacionado às mesas de restaurante.
 */
public class RestaurantTableException extends Exception {
    public RestaurantTableException(String message) {
        super(message);
    }
}
