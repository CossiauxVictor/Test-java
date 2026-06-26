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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account compte1;

    @BeforeEach
    void setUp() {
        compte1 = new Account("FR001", "Alice");
    }

    // ======= Tests creation de compte =======

    @Test
    void shouldCreateAccount_whenNumberDoesNotExist() {
        // Arrange
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(compte1);

        // Act
        Account result = accountService.createAccount("FR001", "Alice");

        // Assert
        assertNotNull(result);
        assertEquals("FR001", result.getNumber());
        assertEquals("Alice", result.getOwner());
        assertEquals(0.0, result.getBalance()); // solde initial = 0
        verify(accountRepository).save(any());
    }

    @Test
    void shouldThrowException_whenNumberAlreadyExists() {
        // Arrange
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(compte1));

        // Act + Assert
        assertThrows(AccountAlreadyExistsException.class,
                () -> accountService.createAccount("FR001", "Bob"));

        verify(accountRepository, never()).save(any());
    }

    // ======= Tests consultation =======

    @Test
    void shouldReturnAccount_whenNumberExists() {
        // Arrange
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(compte1));

        // Act
        Account result = accountService.getAccount("FR001");

        // Assert
        assertNotNull(result);
        assertEquals("FR001", result.getNumber());
    }

    @Test
    void shouldThrowNotFoundException_whenAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findByNumber("INCONNU")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount("INCONNU"));
    }

    @Test
    void shouldReturnAllAccounts() {
        // Arrange
        Account compte2 = new Account("FR002", "Bob");
        when(accountRepository.findAll()).thenReturn(List.of(compte1, compte2));

        // Act
        List<Account> result = accountService.getAllAccounts();

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    // ======= Tests depot =======

    @Test
    void shouldDeposit_whenAmountIsPositive() {
        // Arrange
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(compte1));

        // Act
        Account result = accountService.deposit("FR001", 100.0);

        // Assert
        assertEquals(100.0, result.getBalance());
    }

    @Test
    void shouldThrowException_whenDepositAmountIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit("FR001", 0));
    }

    @Test
    void shouldThrowException_whenDepositAmountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit("FR001", -50));
    }

    // ======= Tests retrait =======

    @Test
    void shouldWithdraw_whenBalanceIsSufficient() {
        // Arrange
        compte1.setBalance(200.0); // on met un solde
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(compte1));

        // Act
        Account result = accountService.withdraw("FR001", 50.0);

        // Assert
        assertEquals(150.0, result.getBalance());
    }

    @Test
    void shouldThrowException_whenWithdrawAmountIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw("FR001", 0));
    }

    @Test
    void shouldThrowException_whenWithdrawAmountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw("FR001", -10));
    }

    @Test
    void shouldThrowInsufficientFunds_whenBalanceIsNotEnough() {
        // Arrange
        compte1.setBalance(30.0); // solde insuffisant
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(compte1));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw("FR001", 100.0));
    }

    // ======= Tests virement =======

    @Test
    void shouldTransfer_whenBothAccountsExistAndBalanceIsSufficient() {
        // Arrange
        Account from = new Account("FR001", "Alice");
        from.setBalance(500.0);
        Account to = new Account("FR002", "Bob");
        to.setBalance(100.0);

        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(accountRepository.findByNumber("FR002")).thenReturn(Optional.of(to));

        // Act
        accountService.transfer("FR001", "FR002", 200.0);

        // Assert
        assertEquals(300.0, from.getBalance());
        assertEquals(300.0, to.getBalance());
    }

    @Test
    void shouldThrowException_whenTransferAmountIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer("FR001", "FR002", 0));
    }

    @Test
    void shouldThrowException_whenTransferAmountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer("FR001", "FR002", -100));
    }

    @Test
    void shouldThrowInsufficientFunds_whenFromAccountBalanceIsNotEnough() {
        // Arrange
        Account from = new Account("FR001", "Alice");
        from.setBalance(50.0); // pas assez
        Account to = new Account("FR002", "Bob");

        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(accountRepository.findByNumber("FR002")).thenReturn(Optional.of(to));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer("FR001", "FR002", 200.0));
    }

    @Test
    void shouldThrowNotFoundException_whenFromAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findByNumber("INCONNU")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("INCONNU", "FR002", 100.0));
    }

    @Test
    void shouldThrowNotFoundException_whenToAccountDoesNotExist() {
        // Arrange
        Account from = new Account("FR001", "Alice");
        from.setBalance(500.0);
        when(accountRepository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(accountRepository.findByNumber("INCONNU")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("FR001", "INCONNU", 100.0));
    }
}
