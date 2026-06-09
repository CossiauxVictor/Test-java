package com.example;

public interface NotificationService {
    void sendConfirmation(String email, ReservationReceipt receipt);
}
