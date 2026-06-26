Feature: Gestion des comptes bancaires

  Scenario: Créer un nouveau compte
    Given aucun compte n'existe
    When je crée un compte avec le numéro "FR001" et le titulaire "Alice"
    Then le statut de la réponse est 201
    And le compte a un solde de 0.0

  Scenario: Déposer de l'argent sur un compte
    Given un compte "FR001" existe avec 100.0 euros
    When je dépose 50.0 euros sur le compte "FR001"
    Then le statut de la réponse est 200
    And le compte a un solde de 150.0

  Scenario: Retirer de l'argent avec succès
    Given un compte "FR001" existe avec 200.0 euros
    When je retire 80.0 euros du compte "FR001"
    Then le statut de la réponse est 200
    And le compte a un solde de 120.0

  Scenario: Retrait refusé pour fonds insuffisants
    Given un compte "FR001" existe avec 30.0 euros
    When je retire 100.0 euros du compte "FR001"
    Then le statut de la réponse est 422

  Scenario: Effectuer un virement entre deux comptes
    Given un compte "FR001" existe avec 500.0 euros
    And un compte "FR002" existe avec 100.0 euros
    When je vire 200.0 euros du compte "FR001" vers le compte "FR002"
    Then le statut de la réponse est 204

  Scenario: Virement refusé pour fonds insuffisants
    Given un compte "FR001" existe avec 50.0 euros
    And un compte "FR002" existe avec 0.0 euros
    When je vire 200.0 euros du compte "FR001" vers le compte "FR002"
    Then le statut de la réponse est 422
