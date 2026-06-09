Feature: Réservation acceptée

  Scenario: Réservation acceptée
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 10
    And aucune réservation existante pour "SALLE-A"
    When "user@example.com" réserve "SALLE-A" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "user@example.com"

  Scenario: Réservation acceptée à capacité maximale
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 10
    And aucune réservation existante pour "SALLE-A"
    When "user@example.com" réserve "SALLE-A" pour 10 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "user@example.com"

  Scenario: Réservation acceptée si le créneau commence après une réservation existante
    Given la salle "SALLE-A" nommée "Salle Alpha" avec une capacité de 10
    And une réservation existante pour "SALLE-A" du "2026-06-10T07:00" au "2026-06-10T09:00"
    When "user@example.com" réserve "SALLE-A" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T11:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "user@example.com"
