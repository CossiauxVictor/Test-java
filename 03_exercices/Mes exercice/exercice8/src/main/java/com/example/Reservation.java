package com.example;

import java.time.LocalDateTime;

public class Reservation {
    private final String userEmail;
    private final String roomCode;
    private final int participantCount;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Reservation(String userEmail, String roomCode, int participantCount,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.userEmail = userEmail;
        this.roomCode = roomCode;
        this.participantCount = participantCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUserEmail() { return userEmail; }
    public String getRoomCode() { return roomCode; }
    public int getParticipantCount() { return participantCount; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
