package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.config.SecurityConfig;
import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.config.security.userdetails.UserDetailsServiceImpl;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.RoleName;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // TODO melhorar as Exceptions

    public UserAuthenticated getUserByEmail(String email) throws Exception{
        try {
            return (UserAuthenticated) userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e){
            throw new Exception("Usuário não encontrado!");
        }
    }

    public User createUser(UserPostDTO dto) throws Exception {
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

        return user;
    }

    private void saveUser(User user){
        userRepository.save(user);
    }
}
