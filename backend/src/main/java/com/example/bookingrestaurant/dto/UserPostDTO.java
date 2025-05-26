package com.example.bookingrestaurant.dto;

import com.example.bookingrestaurant.model.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPostDTO(

        @NotEmpty
        @Size(min = 3, message = "O nome precisa de pelo menos três caracteres")
        String name,

        @NotEmpty
        @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "O email precisa ser válido.")
        String email,

        @NotEmpty
        @Pattern(message = "A senha precisa ter 8 caracteres. 1 Letra minúscula. 1 Letra maiúscula. " +
                "1 caractere especial. 1 número(0/9)",
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
        String password,
        RoleName role
) { }