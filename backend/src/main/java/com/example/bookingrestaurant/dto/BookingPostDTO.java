package com.example.bookingrestaurant.dto;

import java.time.LocalDateTime;

public record BookingPostDTO(Long tableId, LocalDateTime date) {

}
