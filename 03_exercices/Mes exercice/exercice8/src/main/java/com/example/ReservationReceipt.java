package com.example;

import java.time.LocalDateTime;

public class ReservationReceipt {
    private final String roomCode;
    private final String userEmail;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String confirmationMessage;

    public ReservationReceipt(String roomCode, String userEmail,
                               LocalDateTime startTime, LocalDateTime endTime,
                               String confirmationMessage) {
        this.roomCode = roomCode;
        this.userEmail = userEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.confirmationMessage = confirmationMessage;
    }

    public String getRoomCode() { return roomCode; }
    public String getUserEmail() { return userEmail; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getConfirmationMessage() { return confirmationMessage; }
}
