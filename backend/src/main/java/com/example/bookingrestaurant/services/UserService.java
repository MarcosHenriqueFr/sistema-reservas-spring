package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.config.SecurityConfig;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.RoleName;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    // TODO melhorar as Exceptions

    public void createUser(UserPostDTO dto) throws Exception {
        String name = dto.name();
        String email = dto.email();
        String password = securityConfig.passwordEncoder().encode(dto.password());
        RoleName role = dto.role();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        this.saveUser(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
