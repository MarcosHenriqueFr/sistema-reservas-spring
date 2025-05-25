package com.example.bookingrestaurant.config.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe responsável por tratar os erros recebidos da camada Controller.
 * Aceita as Exceções de RestaurantTableException, BookingException e Exceptions genéricas.
 * Além disso, documenta o log de cada Exception.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO Melhorar os logs, fazer um Handler de Bookings e um Handler do Jpa
    /**
     * ExceptionHandler genérico responsável por qualquer exceção inesperada no sistema.
     * Caso a Exceção não possua nenhuma mensagem é associada uma mensagem padrão.
     * Retorna o corpo do erro e o status de 500(Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = e.getMessage() == null ? "Um erro aconteceu." : e.getMessage();

        // TODO Fazer log aqui!

        System.out.println(e.getMessage());

        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), error);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    /**
     * ExceptionHandler responsável pelas exceções vindas do BookingService, sendo assim,
     * as Exceções das reservas.
     * Retorna o corpo do erro e um status genérico 400(Bad Request).
     */
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável pelas Exceções vindas do UserDetailsServiceImpl,
     * quando o usuário não está registrado no banco de dados.
     * Retorna o corpo do erro e o status 401(Unauthorized).
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundInDatabase(UsernameNotFoundException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), e.getLocalizedMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável pelas Exceções de conversão de tipos,
     * quando é enviado um dado que não pode ser convertido.
     * Exceções vindas dos tipos de dado no banco com o request do usuário.
     * Retorna o corpo do erro e o status genérico 400(Bad Request).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Throwable rootCause = ExceptionUtils.getRootCause(e);

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), rootCause.getLocalizedMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável pelas exceções vindas das validações do DTO,
     * principalmente para evitar RollbackExceptions.
     * Através dos nomes dos atributos mostra suas mensagens de erro.
     * Evita que um usuário faça um PATCH ou POST inválido no sistema.
     * Retorna o corpo do erro e um status 422(Unprocessable Entity).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDTO(MethodArgumentNotValidException e){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(
                error -> {
                    String name = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(name, message);
                }
        );

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), errors.toString());

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável por tratar as exceções de RestaurantTableException
     * e suas subclasses, sendo estas exceções customizadas.
     * Retorna o corpo do erro e o status 400(Bad Request).
     */
    @ExceptionHandler(RestaurantTableException.class)
    public ResponseEntity<ErrorResponse> handleRestaurantTableException(RestaurantTableException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável por tratar as exceções dentro da transação do Bean Validation.
     * A partir da exceção, ela seleciona as mensagens das Exceptions e adiciona ao corpo do erro.
     * Retorna o corpo do erro e o status de 422(Unprocessable Entity).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(ConstraintViolationException e){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
        List<String> errorsMessages = errors.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList();

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), errorsMessages.toString());


        return new ResponseEntity<>(errorResponse, status);
    }
}
