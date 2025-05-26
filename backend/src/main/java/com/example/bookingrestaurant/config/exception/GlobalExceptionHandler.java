package com.example.bookingrestaurant.config.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * ExceptionHandler genérico responsável por qualquer exceção inesperada no sistema.
     * Caso a Exceção não possua nenhuma mensagem é associada uma mensagem padrão.
     * Retorna o corpo do erro e o status de 500(Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = "Um erro aconteceu.";

        logger.error("Exceção inesperada aconteceu: {}", e.getMessage());
        logger.error("Trace da Exceção: ", e);

        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), error);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    /**
     * ExceptionHandler responsável pela exceções vindas do UserService,
     * esta sendo a exceção de email já existente no banco de dados.
     * Retorna o corpo do erro e um status 422(Unprocessable Entity).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        logger.error("Exceção de chave duplicada no banco: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

        String error = e.getMessage();

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), error);

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * ExceptionHandler responsável pelas exceções vindas do BookingService, sendo assim,
     * as Exceções das reservas.
     * Retorna o corpo do erro e um status genérico 400(Bad Request).
     */
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        logger.error("Exceção relacionada as reservas: {}", e.getMessage());
        logger.error("Trace da Exceção: ", e);

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

        logger.error("Exceção ao procurar usuário no banco: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

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

        logger.error("Exceção na conversão da Requisição: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

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

        logger.error("Exceção de validação da requisição: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

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

        logger.error("Exceção relacionada as mesas de restaurante: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

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

        logger.error("Exceção de validação: {}", e.getMessage());
        logger.error("Trace da exceção: ", e);

        Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
        List<String> errorsMessages = errors.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList();

        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), status.getReasonPhrase(), errorsMessages.toString());


        return new ResponseEntity<>(errorResponse, status);
    }
}
