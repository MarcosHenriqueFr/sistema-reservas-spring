package com.example.bookingrestaurant.dto;

import com.example.bookingrestaurant.model.RestaurantTableStatus;

public record RestaurantTableDTO(String name, int capacity, RestaurantTableStatus status) {

}
