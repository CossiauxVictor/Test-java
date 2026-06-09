package com.example;

public class Room {
    private final String code;
    private final String name;
    private final int capacity;

    public Room(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
}
