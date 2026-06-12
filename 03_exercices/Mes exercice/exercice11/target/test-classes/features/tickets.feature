Feature: Gestion des tickets de support

  Scenario: Creation d un ticket valide
    Given aucun ticket n existe
    When je cree un ticket avec le titre "Probleme de connexion" et la priorite "HIGH"
    Then la reponse HTTP doit etre 201
    And la reponse contient le titre "Probleme de connexion"
    And le statut du ticket est "OPEN"

  Scenario: Resolution d un ticket en passant par IN_PROGRESS
    Given aucun ticket n existe
    When je cree un ticket avec le titre "Bug critique" et la priorite "HIGH"
    And je modifie le statut du ticket cree vers "IN_PROGRESS"
    Then la reponse HTTP doit etre 200
    And le statut du ticket est "IN_PROGRESS"

  Scenario: Refus de modification d un ticket deja resolu
    Given aucun ticket n existe
    When je cree un ticket avec le titre "Ancien bug" et la priorite "LOW"
    And je modifie le statut du ticket cree vers "RESOLVED"
    And je modifie le statut du ticket cree vers "IN_PROGRESS"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Consultation d un ticket inexistant
    Given aucun ticket n existe
    When je consulte le ticket avec l identifiant 99
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur

  Scenario: Refus de creation avec titre trop court
    Given aucun ticket n existe
    When je cree un ticket avec le titre "AB" et la priorite "MEDIUM"
    Then la reponse HTTP doit etre 400
    And la reponse contient un message d erreur
