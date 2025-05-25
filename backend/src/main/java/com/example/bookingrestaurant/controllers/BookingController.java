package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.dto.BookingPostDTO;
import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsável pela rota de /reservas,
 * tem como principal responsabilidade enviar os dados para a camada de BookingService
 * para a realização da criação, obtenção e soft delete dos dados das reservas.
 * Esse Controller aceita somente métodos do tipo POST, GET e PATCH.
 * */
@RestController
@RequestMapping(path = "reservas")
@Validated
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Mapping responsável pela requisição do tipo POST na rota de /reservas.
     * Recebe os dados da nova reserva e do token JWT, envia ele para a camada de BookingService.
     * Retorna a nova reserva caso ela seja registrada no banco e o Status Http de CREATED.
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingPostDTO data, @AuthenticationPrincipal Jwt jwt) throws Exception {
        Booking booking = bookingService.createBooking(data, jwt.getSubject());
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /**
     * Mapping responsável pela requisição do tipo GET na rota de /reservas.
     * Recebe os dados do token do usuário e envia para camada de BookingService.
     * Retorna suas reservas caso ele tenha e o status Http.
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getBookingFromUser(@AuthenticationPrincipal Jwt jwt) throws Exception {
        List<Booking> bookings = bookingService.getBookingFromUser(jwt.getSubject());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Mapping responsável pela requisição do tipo PATCH na rota de /reservas/:{id}/cancelar.
     * Recebe os dados do id da reserva no banco e
     * o token do usuário, necessário para validar se a reserva é realmente dele
     * retorna a reserva cancelada e o status Http
     */
    @PatchMapping(path = ":{id}/cancelar")
    public ResponseEntity<Booking> softDeleteBooking(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) throws Exception{
        Booking modifiedBooking = bookingService.softDeleteBooking(id, jwt.getSubject());
        return new ResponseEntity<>(modifiedBooking, HttpStatus.OK);
    }
}
