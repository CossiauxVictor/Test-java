Feature: Gestion des comptes bancaires

  Scenario: Creation d un nouveau compte
    Given aucun compte n existe
    When je cree un compte avec le numero "FR001" et le titulaire "Alice"
    Then la reponse HTTP doit etre 201
    And le solde du compte est 0.0

  Scenario: Depot d argent sur un compte
    Given aucun compte n existe
    And un compte "FR001" appartenant a "Alice" avec un solde de 0.0
    When je depose 500.0 sur le compte "FR001"
    Then la reponse HTTP doit etre 200
    And le solde du compte est 500.0

  Scenario: Retrait avec fonds suffisants
    Given aucun compte n existe
    And un compte "FR001" appartenant a "Alice" avec un solde de 300.0
    When je retire 100.0 du compte "FR001"
    Then la reponse HTTP doit etre 200
    And le solde du compte est 200.0

  Scenario: Retrait avec fonds insuffisants
    Given aucun compte n existe
    And un compte "FR001" appartenant a "Alice" avec un solde de 50.0
    When je retire 200.0 du compte "FR001"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Virement entre deux comptes
    Given aucun compte n existe
    And un compte "FR001" appartenant a "Alice" avec un solde de 500.0
    And un compte "FR002" appartenant a "Bob" avec un solde de 100.0
    When j effectue un virement de 200.0 du compte "FR001" vers le compte "FR002"
    Then la reponse HTTP doit etre 200

  Scenario: Virement refuse pour solde insuffisant
    Given aucun compte n existe
    And un compte "FR001" appartenant a "Alice" avec un solde de 50.0
    And un compte "FR002" appartenant a "Bob" avec un solde de 0.0
    When j effectue un virement de 300.0 du compte "FR001" vers le compte "FR002"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
