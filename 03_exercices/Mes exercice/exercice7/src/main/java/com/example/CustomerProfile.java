package com.example;

public enum CustomerProfile {
    STANDARD(0),
    PREMIUM(10),
    VIP(20);

    private final int discountPercent;

    CustomerProfile(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
