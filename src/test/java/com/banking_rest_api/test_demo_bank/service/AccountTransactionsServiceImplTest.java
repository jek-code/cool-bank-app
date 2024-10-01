package com.banking_rest_api.test_demo_bank.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import com.banking_rest_api.test_demo_bank.service.impl.AccountTransactionsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class AccountTransactionsServiceImplTest {

    @InjectMocks
    private AccountTransactionsServiceImpl accountTransactionsService;

    @Mock
    private AccountManagementService managementService;

    @Mock
    private TransactionsRepository transRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAllTransactionsForAccountID() {
        Long accountId = 1L;
        Transaction transaction1 = new Transaction();
        transaction1.setAccount(accountId);
        transaction1.setSum(BigDecimal.valueOf(100));

        Transaction transaction2 = new Transaction();
        transaction2.setAccount(accountId);
        transaction2.setSum(BigDecimal.valueOf(200));

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transRepo.findByAccount(accountId)).thenReturn(transactions);

        List<Transaction> result = accountTransactionsService.allTransactionsForAccountID(accountId);

        assertEquals(2, result.size());
        verify(transRepo, times(1)).findByAccount(accountId);
    }

    @Test
    public void testDeposit() {
        Transaction transaction = new Transaction();
        transaction.setAccount(1L);
        transaction.setSum(BigDecimal.valueOf(100));
        transaction.setOrderID("order1");

        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setFirst_name("John");
        account.setLast_name("Doe");

        when(managementService.getById(1L)).thenReturn(account);

        TransactionResponse response = accountTransactionsService.deposit(transaction);

        assertNotNull(response);
        assertTrue(response.isWasSuccessful());
        assertEquals("order1", response.getTransactionID());
        assertEquals("100$ was sent to John Doe", response.getDescription());

        assertEquals(BigInteger.valueOf(300), account.getBalance());
        verify(managementService, times(1)).saveAccount(account);
        verify(transRepo, times(1)).save(transaction);
    }

    @Test
    public void testWithdraw_Success() {
        Transaction transaction = new Transaction();
        transaction.setAccount(1L);
        transaction.setSum(BigDecimal.valueOf(100));
        transaction.setOrderID("order1");

        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setFirst_name("John");
        account.setLast_name("Doe");

        when(managementService.getById(1L)).thenReturn(account);

        TransactionResponse response = accountTransactionsService.withdraw(transaction);

        assertNotNull(response);
        assertTrue(response.isWasSuccessful());
        assertEquals("order1", response.getTransactionID());
        assertEquals("100$ withdrawn from account of John Doe", response.getDescription());

        assertEquals(BigInteger.valueOf(100), account.getBalance());
        verify(managementService, times(1)).saveAccount(account);
        verify(transRepo, times(1)).save(transaction);
    }

    @Test
    public void testWithdraw_InsufficientFunds() {
        Transaction transaction = new Transaction();
        transaction.setAccount(1L);
        transaction.setSum(BigDecimal.valueOf(300)); // More than the current balance
        transaction.setOrderID("order1");

        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setFirst_name("John");
        account.setLast_name("Doe");

        when(managementService.getById(1L)).thenReturn(account);

        TransactionResponse response = accountTransactionsService.withdraw(transaction);

        assertNull(response);
        assertEquals(BigInteger.valueOf(200), account.getBalance()); // Balance should not change
        verify(managementService, never()).saveAccount(any());
        verify(transRepo, never()).save(any());
    }
}