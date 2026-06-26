package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    private Account alice;

    @BeforeEach
    void setUp() {
        alice = new Account("FR001", "Alice");
    }

    // --- Création ---

    @Test
    void create_nouveau_compte_retourne_compte_avec_solde_zero() {
        when(repository.existsByNumber("FR001")).thenReturn(false);
        when(repository.save(any())).thenReturn(alice);

        Account result = service.create("FR001", "Alice");

        assertThat(result.getNumber()).isEqualTo("FR001");
        assertThat(result.getBalance()).isEqualTo(0);
    }

    @Test
    void create_compte_deja_existant_leve_exception() {
        when(repository.existsByNumber("FR001")).thenReturn(true);

        assertThatThrownBy(() -> service.create("FR001", "Alice"))
                .isInstanceOf(AccountAlreadyExistsException.class);
    }

    // --- Consultation ---

    @Test
    void getByNumber_compte_existant_retourne_compte() {
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));

        Account result = service.getByNumber("FR001");

        assertThat(result.getHolder()).isEqualTo("Alice");
    }

    @Test
    void getByNumber_compte_inexistant_leve_exception() {
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByNumber("FR999"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void findAll_retourne_tous_les_comptes() {
        Account bob = new Account("FR002", "Bob");
        when(repository.findAll()).thenReturn(List.of(alice, bob));

        List<Account> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    // --- Dépôt ---

    @Test
    void deposit_montant_valide_augmente_solde() {
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));
        when(repository.save(any())).thenReturn(alice);

        Account result = service.deposit("FR001", 100.0);

        assertThat(result.getBalance()).isEqualTo(100.0);
    }

    @Test
    void deposit_montant_nul_leve_exception() {
        assertThatThrownBy(() -> service.deposit("FR001", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deposit_montant_negatif_leve_exception() {
        assertThatThrownBy(() -> service.deposit("FR001", -50))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // --- Retrait ---

    @Test
    void withdraw_montant_valide_diminue_solde() {
        alice.deposit(200.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));
        when(repository.save(any())).thenReturn(alice);

        Account result = service.withdraw("FR001", 50.0);

        assertThat(result.getBalance()).isEqualTo(150.0);
    }

    @Test
    void withdraw_montant_nul_leve_exception() {
        assertThatThrownBy(() -> service.withdraw("FR001", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_montant_negatif_leve_exception() {
        assertThatThrownBy(() -> service.withdraw("FR001", -10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_fonds_insuffisants_leve_exception() {
        alice.deposit(30.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));

        assertThatThrownBy(() -> service.withdraw("FR001", 100.0))
                .isInstanceOf(InsufficientFundsException.class);
    }

    // --- Virement ---

    @Test
    void transfer_valide_deplace_argent_entre_comptes() {
        alice.deposit(500.0);
        Account bob = new Account("FR002", "Bob");
        bob.deposit(100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(bob));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.transfer("FR001", "FR002", 200.0);

        assertThat(alice.getBalance()).isEqualTo(300.0);
        assertThat(bob.getBalance()).isEqualTo(300.0);
    }

    @Test
    void transfer_montant_nul_leve_exception() {
        assertThatThrownBy(() -> service.transfer("FR001", "FR002", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void transfer_montant_negatif_leve_exception() {
        assertThatThrownBy(() -> service.transfer("FR001", "FR002", -100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void transfer_fonds_insuffisants_leve_exception() {
        alice.deposit(50.0);
        Account bob = new Account("FR002", "Bob");
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(bob));

        assertThatThrownBy(() -> service.transfer("FR001", "FR002", 200.0))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void transfer_compte_source_inexistant_leve_exception() {
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.transfer("FR999", "FR002", 100.0))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void transfer_compte_destination_inexistant_leve_exception() {
        alice.deposit(500.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(alice));
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.transfer("FR001", "FR999", 100.0))
                .isInstanceOf(AccountNotFoundException.class);
    }
}
