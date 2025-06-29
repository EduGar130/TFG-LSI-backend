package com.tfg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalExceptionHandler {

    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas.");
    }

    public ResponseEntity<?> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado.");
    }

    public ResponseEntity<Object> manejarStockInsuficiente(StockInsuficienteException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Stock insuficiente");
        cuerpo.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> buildResponse(HttpStatus status, String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", mensaje);
        return new ResponseEntity<>(error, status);
    }
}
