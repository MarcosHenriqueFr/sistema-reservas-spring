package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.config.exception.UserAlreadyExistsException;
import com.example.bookingrestaurant.config.security.auth.AuthenticationService;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pelo controle de alto nivel requisições para a rota de /usuarios,
 * responsável por enviar o registro de novos usuários e da parte de login
 * para a camada de UserService/AuthenticationService.
 * Esse Controller só aceita métodos do tipo POST.
 * Essa rota não precisa de autenticação do usuário para ser acessada
 * */
@RestController
@RequestMapping(path = "usuarios")
@Validated
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    /**
     * Mapping responsável pela requisição do tipo POST na rota de /usuarios/login.
     * É responsável pelo recebimento das informações do usuário e retornar o token JWT
     * */
    @PostMapping("login")
    public ResponseEntity<String> authenticate(Authentication authentication){
        String token = authenticationService.authenticate(authentication);
        logger.info("Usuário autenticado: {}.", authentication.getName());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * Mapping responsável pela requisição do tipo POST na rota de /usuarios/registrar.
     * É responsável pelo recebimento das informações do usuário e envio para a camada de UserService
     * retorna o usuário se ele for registrado no banco
     * */
    @PostMapping("registrar")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserPostDTO userData) throws UserAlreadyExistsException {
        User user = userService.createUser(userData);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
