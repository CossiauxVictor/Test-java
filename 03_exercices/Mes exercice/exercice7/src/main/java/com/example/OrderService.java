package com.example;

import java.util.Optional;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderReceipt placeOrder(String customerEmail, CustomerProfile profile, String productRef, int quantity) {
        Optional<Product> productOpt = productRepository.findByReference(productRef);
        if (productOpt.isEmpty()) {
            throw new OrderException("Produit inconnu");
        }

        Product product = productOpt.get();
        if (quantity > product.getStock()) {
            throw new OrderException("Stock insuffisant");
        }

        double discount = profile.getDiscountPercent() / 100.0;
        double total = product.getUnitPrice() * quantity * (1 - discount);

        return new OrderReceipt(productRef, quantity, total, "Commande confirmée");
    }
}
