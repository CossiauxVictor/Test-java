Feature: Commande refusée

  Scenario: Commande refusée si produit inconnu
    Given aucun produit n'existe avec la référence "PROD-999"
    When le client "client@example.com" de profil STANDARD commande 1 unité(s) de "PROD-999"
    Then la commande est refusée
    And le motif du refus est "Produit inconnu"

  Scenario: Commande refusée si stock insuffisant
    Given un produit "PROD-001" nommé "Laptop" au prix de 1000.0 avec un stock de 2
    When le client "client@example.com" de profil STANDARD commande 5 unité(s) de "PROD-001"
    Then la commande est refusée
    And le motif du refus est "Stock insuffisant"
