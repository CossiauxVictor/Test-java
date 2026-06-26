package com.example.bankapi.controller;

import com.example.bankapi.dto.ApiError;
import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BankApiExceptionHandler {

    // compte pas trouve -> 404
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(AccountNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage()));
    }

    // compte deja existant -> 409
    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(AccountAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, ex.getMessage()));
    }

    // solde insuffisant -> 409
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, ex.getMessage()));
    }

    // erreur de validation -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Requete invalide");

        return ResponseEntity
                .badRequest()
                .body(ApiError.of(400, message));
    }

    // montant invalide -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiError.of(400, ex.getMessage()));
    }
}
