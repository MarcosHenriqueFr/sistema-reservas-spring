package com.example.bookingrestaurant.config.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service dedicado para a geração de tokens para o Usuário.
 * Possui um atributo para modificar os dados: JwtService
 */
@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    public String authenticate(Authentication authentication){
        return jwtService.generateToken(authentication);
    }
}
