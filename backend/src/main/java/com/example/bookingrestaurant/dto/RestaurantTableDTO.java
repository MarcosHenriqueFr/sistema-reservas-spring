package com.example.bookingrestaurant.dto;

import com.example.bookingrestaurant.model.RestaurantTableStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record RestaurantTableDTO(
        @Size(min = 3, message = "O código da mesa precisa de pelo menos três caracteres")
        @NotEmpty(message = "Informe o nome/código da mesa")
        String name,

        @Range(min = 2, max = 20, message = "A capacidade da mesa tem que ser entre 2 e 20")
        int capacity,

        RestaurantTableStatus status
) { }
