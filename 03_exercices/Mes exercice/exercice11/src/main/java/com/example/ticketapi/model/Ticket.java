package com.example.ticketapi.model;

// classe qui represente un ticket de support
public class Ticket {

    private Long id;
    private String title;
    private Priority priority;
    private Status status;

    public Ticket() {
    }

    public Ticket(Long id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.OPEN; // par defaut c'est OPEN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
