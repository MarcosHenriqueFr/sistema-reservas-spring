package com.example.bookingrestaurant.dto;

import com.example.bookingrestaurant.model.RoleName;

public record UserPostDTO(String name, String email, String password, RoleName role) {
}
