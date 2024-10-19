package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.service.TransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionsControllerTest {

    @Mock
    private TransactionsService transactionsService;

    @Mock
    private RequestToTransactionMapper mapper;

    @InjectMocks
    private TransactionsController transactionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit_Success() {
        ClientRequest request = new ClientRequest(1L, BigDecimal.valueOf(100.0));
        Transaction transaction = new Transaction(); // Create mock transaction if needed
        TransactionResponse response = new TransactionResponse(true, "Deposit successful", "description");

        // Mock the mapper and service behavior
        when(mapper.depositTransaction(any(ClientRequest.class))).thenReturn(transaction);
        when(transactionsService.deposit(transaction)).thenReturn(response);

        // Call the controller method
        ResponseEntity<TransactionResponse> result = transactionsController.deposit(request);

        // Verify response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testWithdraw_Success() {
        ClientRequest request = new ClientRequest(1L, BigDecimal.valueOf(50.0));
        Transaction transaction = new Transaction();
        TransactionResponse response = new TransactionResponse(true, "Withdrawal successful", "description");

        // Mock the mapper and service behavior
        when(mapper.withdrawTransaction(any(ClientRequest.class))).thenReturn(transaction);
        when(transactionsService.withdraw(transaction)).thenReturn(response);

        // Call the controller method
        ResponseEntity<TransactionResponse> result = transactionsController.withdraw(request);

        // Verify response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testTransfer_Success() {
        ClientTransferRequest request = new ClientTransferRequest(1L, 2L, BigDecimal.valueOf(150.0));
        Transaction outgoingTransaction = new Transaction();
        Transaction incomingTransaction = new Transaction();
        List<Transaction> transferTransaction = List.of(outgoingTransaction, incomingTransaction);
        TransactionResponse response = new TransactionResponse(true, "Transfer successful", "description");


        // Mock the mapper and service behavior
        when(mapper.transferTransactions(any(ClientTransferRequest.class))).thenReturn(transferTransaction);
        when(transactionsService.transfer(transferTransaction)).thenReturn(response);

        // Call the controller method
        ResponseEntity<TransactionResponse> result = transactionsController.transfer(request);

        // Verify response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}