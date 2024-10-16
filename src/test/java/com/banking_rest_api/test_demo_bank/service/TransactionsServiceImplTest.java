package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import com.banking_rest_api.test_demo_bank.service.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionsServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private TransactionsServiceImpl accountTransactionsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAllTransactionsForAccountID() {
        Long accountId = 1L;
        List<Transaction> transactions = List.of(
                Transaction.builder().account(accountId).orderID(UUID.randomUUID().toString()).build(),
                Transaction.builder().account(accountId).orderID(UUID.randomUUID().toString()).build()
        );

        when(transactionsRepository.findByAccount(accountId)).thenReturn(transactions);

        List<Transaction> result = accountTransactionsService.allTransactionsForAccountID(accountId);

        assertEquals(transactions.size(), result.size());
        assertEquals(transactions.get(0).getOrderID(), result.get(0).getOrderID());
        verify(transactionsRepository, times(1)).findByAccount(accountId);
    }

    @Test
    public void testSaveTransaction() {
        Transaction transaction = Transaction.builder()
                .account(1L)
                .orderID(UUID.randomUUID().toString())
                .build();

        when(transactionsRepository.save(transaction)).thenReturn(transaction);

        TransactionResponse response = accountTransactionsService.saveTransaction(transaction);

        assertNotNull(response);
        assertTrue(response.isWasSuccessful());
        assertEquals(transaction.getOrderID(), response.getTransactionID());
        verify(transactionsRepository, times(1)).save(transaction);
    }
}