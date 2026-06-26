package com.example.bankapi.model;

// classe qui represente un compte bancaire
public class Account {

    private String number;
    private String owner;
    private double balance;

    public Account() {
    }

    public Account(String number, String owner) {
        this.number = number;
        this.owner = owner;
        this.balance = 0.0; // le solde initial est toujours 0
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
