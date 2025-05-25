package com.example.bookingrestaurant.config.exception;

import java.time.LocalDateTime;

/**
 * Classe responsável por representar o corpo do erro,
 * seu principal objetivo é padronizar as respostas dos ResponseEntities.
 * Possui atributos TIMESTAMP para determinar o tempo do erro,
 * atributo de STATUS para receber o status http,
 * stributo de ERROR que recebe a mensagem do status http,
 * e o atributo de MESSAGE que recebe a mensagem da Exception.
 */
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message){
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
