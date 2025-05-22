package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.dto.BookingPostDTO;
import com.example.bookingrestaurant.model.Booking;
import com.example.bookingrestaurant.model.BookingStatus;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.model.RestaurantTableStatus;
import com.example.bookingrestaurant.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsável por receber os dados passados pelo BookingController,
 * sendo possível criar, salvar, obter e modificar os dados da reserva diretamente com o banco.
 * Possui três atributos para obtenção de dados: UserService, RestaurantTableService e BookingRepository.
 * Lança as exceções relacionadas com as reservas, usuários e mesas de restaurante.
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    // TODO - Especificar as Exceptions de Create e find, somente Exceptions temporárias

    /**
     * Method responsável por criar uma nova reserva.
     * Recebe os parametros dos dados da nova reserva e o email do usuário.
     * Além das validações da mesa e de usuário,
     * valida se a data da Reserva é valida e modifica os dados da reserva e da mesa.
     * Por fim, salva o usuário e retorna o dado da reserva.
     */
    public Booking createBooking(BookingPostDTO newBooking, String subject) throws  Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        RestaurantTable table = restaurantTableService.findTableById(newBooking.tableId());
        restaurantTableService.checkTableValidation(table);

        boolean validTime = this.checkValidBookingDate(newBooking.date());
        if(!validTime){
            throw new Exception("Data Inválida");
        }

        Booking booking = new Booking();
        booking.setUser(userAuthenticated.getUser());
        booking.setTable(table);
        booking.setBookingDate(newBooking.date());
        booking.setBookingStatus(BookingStatus.ACTIVE);

        table.setStatus(RestaurantTableStatus.BOOKED);

        saveBooking(booking);

        return booking;
    }

    /**
     * Method responsável por checar se uma data de Reserva é valida,
     * o parâmetro para validação é do período de 30 dias.
     * Retorna um valor booleano correspondente a sua validação.
     */
    private boolean checkValidBookingDate(LocalDateTime userDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime supportedDate = now.plusMonths(1);

        return userDate.isAfter(now) && !userDate.isAfter(supportedDate);
    }

    /**
     * Method privado responsável por salvar os dados da Reserva diretamente no banco de dados.
     * Recebe a entidade de Booking para fazer o commit.
     */
    private void saveBooking(Booking booking){
        bookingRepository.save(booking);
    }

    /**
     * Method privado responsável por procurar uma reserva pelo seu id.
     * Caso não seja encontrada, joga uma exceção com reserva não encontrada.
     */
    private Booking findBookingById(Long id) throws Exception {
        return bookingRepository.findBookingById(id)
                .orElseThrow(() -> new Exception("Reserva não encontrada"));
    }

    /**
     * Method responsável por procurar uma lista de reservas a partir de um usuário.
     * Recebe um email do usuário(do token) e converte em um Usuário autenticado.
     * Retorna a lista ou null caso o usuário não possua reservas.
     */
    public List<Booking> getBookingFromUser(String subject) throws Exception {
        UserAuthenticated userAuthenticated = userService.getUserByEmail(subject);
        return bookingRepository.findByUser(userAuthenticated.getUser());
    }

    /**
     * Method responsável por mudar o status de uma reserva de Active para Canceled.
     * Recebe o id da reserva e o email do usuário(do token).
     * Faz as validações para modificar somente a lista do usuário autenticado
     * Salva a nova reserva cancelada e retorna a reserva.
     */
    public Booking softDeleteBooking(Long id, String subject) throws Exception {
        Booking booking = this.findBookingById(id);

        List<Booking> userBookings = this.getBookingFromUser(subject);

        if(userBookings.isEmpty()){
            throw new Exception("O usuário não fez nenhuma reserva");
        }

        if(!userBookings.contains(booking)){
            throw new Exception("Reserva não encontrada");
        }

        RestaurantTable table = booking.getTable();
        table.setStatus(RestaurantTableStatus.AVAILABLE);
        booking.setBookingStatus(BookingStatus.CANCELED);

        this.saveBooking(booking);

        return booking;
    }
}
