package com.example;

public class PriceCalculator {

    public double calculateTotalPrice(double unitPrice, int quantity) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Le prix unitaire ne doit pas être négatif");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("La quantité ne doit pas être négative");
        }
        return unitPrice * quantity;
    }

    public double applyDiscount(double price, double discountRate) {
        if (discountRate < 0) {
            throw new IllegalArgumentException("Le taux de remise ne doit pas être négatif");
        }
        return price * (1 - discountRate);
    }

    public double calculateVat(double price, double vatRate) {
        if (vatRate < 0) {
            throw new IllegalArgumentException("Le taux de TVA ne doit pas être négatif");
        }
        return price * vatRate;
    }

    public double calculatePriceWithVat(double price, double vatRate) {
        if (vatRate < 0) {
            throw new IllegalArgumentException("Le taux de TVA ne doit pas être négatif");
        }
        return price + price * vatRate;
    }
}
