Feature: Réservations d'ouvrages MédiaCity

  Scenario: Réservation d'un ouvrage indisponible
    Given un adhérent "Alice" non suspendu
    And un ouvrage "Le Petit Prince" indisponible
    When l'adhérent réserve l'ouvrage
    Then la réservation est créée avec succès

  Scenario: Refus d'une réservation pour un adhérent suspendu
    Given un adhérent "Charlie" suspendu
    And un ouvrage "1984" indisponible
    When l'adhérent réserve l'ouvrage
    Then la réservation est refusée pour suspension

  Scenario: Plusieurs réservations sur le même ouvrage
    Given un ouvrage "Les Misérables" indisponible
    When "Alice" réserve cet ouvrage
    And "Bob" réserve cet ouvrage
    Then il y a 2 réservations en attente

  Scenario: Restitution d'un ouvrage réservé
    Given un ouvrage "Dune" indisponible
    And l'adhérent "Alice" a déjà réservé cet ouvrage
    When l'ouvrage est rendu
    Then la réservation de "Alice" est retournée

  Scenario: Un adhérent ne peut pas réserver un ouvrage disponible
    Given un adhérent "Bob" non suspendu
    And un ouvrage "Harry Potter" disponible
    When l'adhérent réserve l'ouvrage
    Then la réservation est refusée car l'ouvrage est disponible
