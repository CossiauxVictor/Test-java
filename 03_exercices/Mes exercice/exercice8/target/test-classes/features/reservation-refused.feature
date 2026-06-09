Feature: Réservation refusée

  Scenario: Réservation refusée si salle inconnue
    Given aucune salle n'existe avec le code "SALLE-Z"
    When "user@example.com" réserve "SALLE-Z" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est refusée
    And le motif du refus est "Salle inconnue"
    And aucune confirmation n'est envoyée

  Scenario: Réservation refusée si capacité insuffisante
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 5
    And aucune réservation existante pour "SALLE-A"
    When "user@example.com" réserve "SALLE-A" pour 10 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est refusée
    And le motif du refus est "Capacité insuffisante"
    And aucune confirmation n'est envoyée

  Scenario: Réservation refusée si période invalide
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 10
    And aucune réservation existante pour "SALLE-A"
    When "user@example.com" réserve "SALLE-A" pour 5 participants du "2026-06-10T11:00" au "2026-06-10T09:00"
    Then la réservation est refusée
    And le motif du refus est "Période invalide"
    And aucune confirmation n'est envoyée

  Scenario: Réservation refusée si conflit de réservation
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 10
    And une réservation existante pour "SALLE-A" du "2026-06-10T08:00" au "2026-06-10T11:00"
    When "user@example.com" réserve "SALLE-A" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T12:00"
    Then la réservation est refusée
    And le motif du refus est "Créneau non disponible"
    And aucune confirmation n'est envoyée
