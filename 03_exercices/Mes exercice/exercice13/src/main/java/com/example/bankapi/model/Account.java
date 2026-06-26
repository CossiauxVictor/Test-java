package com.example.bankapi.model;

public class Account {
    private String number;
    private String holder;
    private double balance;

    public Account(String number, String holder) {
        this.number = number;
        this.holder = holder;
        this.balance = 0;
    }

    public String getNumber() { return number; }
    public String getHolder() { return holder; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }
}
