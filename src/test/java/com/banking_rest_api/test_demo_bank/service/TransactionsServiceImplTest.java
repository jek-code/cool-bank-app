package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import com.banking_rest_api.test_demo_bank.service.impl.TransactionsServiceImpl;
import com.banking_rest_api.test_demo_bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionsServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));

        transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setAmount(BigDecimal.valueOf(50.00));
        transaction.setOrderId("ORDER123");
    }

    @Test
    void testDepositSuccessful() {
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.of(account));
        transaction.setType(TransactionType.DEPOSIT);

        TransactionResponse response = transactionsService.deposit(transaction);

        assertEquals(true, response.isSuccessful());
        assertEquals("ORDER123", response.getTransactionID());
        assertEquals(BigDecimal.valueOf(150.00), account.getBalance());
        verify(transactionsRepository, times(1)).save(transaction);
    }

    @Test
    void testDepositAccountNotFound() {
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.empty());
        transaction.setType(TransactionType.DEPOSIT);


        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            transactionsService.deposit(transaction);
        });

        assertEquals("Account with ID 1 not found", exception.getMessage());
    }

    @Test
    void testWithdrawSuccessful() {
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.of(account));
        transaction.setType(TransactionType.WITHDRAW);

        TransactionResponse response = transactionsService.withdraw(transaction);

        assertEquals(true, response.isSuccessful());
        assertEquals("ORDER123", response.getTransactionID());
        assertEquals(BigDecimal.valueOf(50.00), account.getBalance());
        verify(transactionsRepository, times(1)).save(transaction);
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        // Arrange
        transaction.setAmount(BigDecimal.valueOf(1200.00));  // Withdraw more than balance
        transaction.setType(TransactionType.WITHDRAW);
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.of(account));

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> transactionsService.withdraw(transaction));
        verify(accountRepository, never()).save(any());  // Account should not be saved
        verify(transactionsRepository, never()).save(any());  // Transaction should not be saved
    }

    @Test
    void testWithdrawAccountNotFound() {
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.empty());
        transaction.setType(TransactionType.WITHDRAW);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            transactionsService.withdraw(transaction);
        });

        assertEquals("Account with ID 1 not found", exception.getMessage());
    }

    @Test
    void testTransferSuccessful() {
        var accReceiver = new Account();
        accReceiver.setBalance(BigDecimal.valueOf(50.0));
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findWithoutTransactionsById(2L)).thenReturn(Optional.of(accReceiver));

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setAccountId(1L);
        withdrawTransaction.setAmount(BigDecimal.valueOf(50.00));
        withdrawTransaction.setOrderId("WITHDRAW_ORDER");
        withdrawTransaction.setType(TransactionType.OUTGOING_TRANSFER);

        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccountId(2L);
        depositTransaction.setAmount(BigDecimal.valueOf(50.00));
        depositTransaction.setOrderId("DEPOSIT_ORDER");
        depositTransaction.setType(TransactionType.INCOMING_TRANSFER);

        List<Transaction> transactions = List.of(withdrawTransaction, depositTransaction);

        TransactionResponse response = transactionsService.transfer(transactions);

        assertEquals(true, response.isSuccessful());
        assertEquals("DEPOSIT_ORDER", response.getTransactionID());
        assertEquals(BigDecimal.valueOf(50.00), account.getBalance());
        verify(transactionsRepository, times(1)).save(withdrawTransaction);
        verify(transactionsRepository, times(1)).save(depositTransaction);
    }

    @Test
    void testTransferWithdrawFailed() {
        when(accountRepository.findWithoutTransactionsById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findWithoutTransactionsById(2L)).thenReturn(Optional.of(new Account()));

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setAccountId(1L);
        withdrawTransaction.setAmount(BigDecimal.valueOf(200.00)); // Withdraw more than the balance
        withdrawTransaction.setOrderId("WITHDRAW_ORDER");
        withdrawTransaction.setType(TransactionType.OUTGOING_TRANSFER);

        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccountId(2L);
        depositTransaction.setAmount(BigDecimal.valueOf(50.00));
        depositTransaction.setOrderId("DEPOSIT_ORDER");
        depositTransaction.setType(TransactionType.INCOMING_TRANSFER);

        List<Transaction> transactions = List.of(withdrawTransaction, depositTransaction);
        assertThrows(InsufficientFundsException.class, () -> transactionsService.transfer(transactions));

        verify(transactionsRepository, never()).save(withdrawTransaction);
        verify(transactionsRepository, never()).save(depositTransaction);
    }
}