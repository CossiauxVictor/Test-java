Feature: Bowling Frame

  Scenario: Premier lancer augmente le score d'une série standard
    Given une série standard est en cours
    When le joueur effectue un lancer de 5 quilles
    Then le score de la série est 5

  Scenario: Second lancer augmente le score d'une série standard
    Given une série standard est en cours
    When le joueur effectue un lancer de 3 quilles
    And le joueur effectue un lancer de 4 quilles
    Then le score de la série est 7

  Scenario: Un strike empêche un second lancer dans une série standard
    Given une série standard est en cours
    When le joueur fait un strike
    Then le joueur ne peut pas lancer à nouveau dans cette série

  Scenario: Deux lancers empêchent un troisième lancer dans une série standard
    Given une série standard est en cours avec 2 lancers normaux
    Then le joueur ne peut pas lancer à nouveau dans cette série

  Scenario: Strike dans le dernier round - le second lancer est accepté
    Given une dernière série est en cours
    When le joueur fait un strike
    Then le joueur peut lancer à nouveau dans cette série

  Scenario: Strike dans le dernier round - le score augmente après un second lancer
    Given une dernière série est en cours
    When le joueur fait un strike
    And le joueur effectue un lancer de 5 quilles
    Then le score de la série est 15

  Scenario: Strike dans le dernier round - le troisième lancer est accepté
    Given une dernière série est en cours avec un strike et un lancer de 5
    Then le joueur peut lancer à nouveau dans cette série

  Scenario: Strike dans le dernier round - le score augmente au troisième lancer
    Given une dernière série est en cours avec un strike et un lancer de 5
    When le joueur effectue un lancer de 3 quilles
    Then le score de la série est 18

  Scenario: Spare dans le dernier round - le troisième lancer est accepté
    Given une dernière série est en cours avec un spare
    Then le joueur peut lancer à nouveau dans cette série

  Scenario: Spare dans le dernier round - le score augmente au troisième lancer
    Given une dernière série est en cours avec un spare
    When le joueur effectue un lancer de 6 quilles
    Then le score de la série est 16

  Scenario: Pas de troisième lancer dans le dernier round sans strike ni spare
    Given une dernière série est en cours avec 2 lancers normaux
    Then le joueur ne peut pas lancer à nouveau dans cette série

  Scenario: Pas de quatrième lancer dans le dernier round
    Given une dernière série est en cours avec un strike et deux lancers
    Then le joueur ne peut pas lancer à nouveau dans cette série
