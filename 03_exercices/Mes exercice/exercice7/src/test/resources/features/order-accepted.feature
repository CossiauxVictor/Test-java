Feature: Commande acceptée

  Scenario: Commande acceptée pour client STANDARD
    Given un produit "PROD-001" nommé "Laptop" au prix de 1000.0 avec un stock de 10
    When le client "client@example.com" de profil STANDARD commande 2 unité(s) de "PROD-001"
    Then la commande est acceptée
    And le montant total est 2000.0
    And le message de confirmation contient "Commande confirmée"

  Scenario: Commande acceptée pour client PREMIUM
    Given un produit "PROD-001" nommé "Laptop" au prix de 1000.0 avec un stock de 10
    When le client "premium@example.com" de profil PREMIUM commande 2 unité(s) de "PROD-001"
    Then la commande est acceptée
    And le montant total est 1800.0
    And le message de confirmation contient "Commande confirmée"

  Scenario: Commande acceptée pour client VIP
    Given un produit "PROD-001" nommé "Laptop" au prix de 1000.0 avec un stock de 10
    When le client "vip@example.com" de profil VIP commande 2 unité(s) de "PROD-001"
    Then la commande est acceptée
    And le montant total est 1600.0
    And le message de confirmation contient "Commande confirmée"
