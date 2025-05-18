package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.config.security.auth.AuthenticationService;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "usuarios")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<String> authenticate(Authentication authentication){
        String token = authenticationService.authenticate(authentication);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("registrar")
    public ResponseEntity<User> createUser(@RequestBody UserPostDTO userData) throws Exception {
        User user = userService.createUser(userData);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
