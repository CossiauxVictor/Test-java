package com.example.ticketapi.exception;

import com.example.ticketapi.model.Status;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(Status current, Status target) {
        super("Transition de statut interdite : " + current + " -> " + target);
    }
}
