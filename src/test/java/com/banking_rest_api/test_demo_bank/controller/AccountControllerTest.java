package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountManagementService accountManagementService;

    @Mock
    private RequestToTransactionMapper mapper;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNewAccount() {
        Account account = new Account();
        AccountCreatedResponse expectedResponse = AccountCreatedResponse.builder()
                .wasSuccessful(true)
                .description("Account created")
                .build();

        when(accountManagementService.saveAccount(account)).thenReturn(expectedResponse);

        ResponseEntity<AccountCreatedResponse> response = accountController.createNewAccount(account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(accountManagementService, times(1)).saveAccount(account);
    }

    @Test
    public void testGetAccById() {
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO(accountId, "John", "Dddd", null, BigDecimal.valueOf(1000), List.of());

        when(accountManagementService.getById(accountId)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
        verify(accountManagementService, times(1)).getById(accountId);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accounts = List.of(new Account(), new Account());

        when(accountManagementService.getAllUsers()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts.size(), response.getBody().size());
        verify(accountManagementService, times(1)).getAllUsers();
    }

    @Test
    public void testDeposit() {
        ClientRequest clientRequest = new ClientRequest(1L, BigDecimal.valueOf(1000));
        Transaction transaction = Transaction.builder().orderID(UUID.randomUUID().toString()).sum(clientRequest.getSum()).build();
        TransactionResponse transactionResponse = TransactionResponse.builder().wasSuccessful(true).transactionID(transaction.getOrderID()).build();

        when(mapper.depositTransaction(clientRequest)).thenReturn(transaction);
        when(accountManagementService.deposit(transaction)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = accountController.deposit(clientRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(mapper, times(1)).depositTransaction(clientRequest);
        verify(accountManagementService, times(1)).deposit(transaction);
    }

    @Test
    public void testWithdraw() {
        ClientRequest clientRequest = new ClientRequest(1L, BigDecimal.valueOf(500));
        Transaction transaction = Transaction.builder().orderID(UUID.randomUUID().toString()).sum(clientRequest.getSum()).build();
        TransactionResponse transactionResponse = TransactionResponse.builder().wasSuccessful(true).transactionID(transaction.getOrderID()).build();

        when(mapper.withdrawTransaction(clientRequest)).thenReturn(transaction);
        when(accountManagementService.withdraw(transaction)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = accountController.withdraw(clientRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(mapper, times(1)).withdrawTransaction(clientRequest);
        verify(accountManagementService, times(1)).withdraw(transaction);
    }

    @Test
    public void testTransfer() {
        ClientTransferRequest transferRequest = new ClientTransferRequest(1L, 2L, BigDecimal.valueOf(100));
        Transaction outgoingTransaction = Transaction.builder().orderID(UUID.randomUUID().toString()).sum(transferRequest.getSum()).build();
        Transaction incomingTransaction = Transaction.builder().orderID(UUID.randomUUID().toString()).sum(transferRequest.getSum()).build();
        List<Transaction> transactions = List.of(outgoingTransaction, incomingTransaction);
        TransactionResponse transactionResponse = TransactionResponse.builder().wasSuccessful(true).transactionID(outgoingTransaction.getOrderID()).build();

        when(mapper.transferTransactions(transferRequest)).thenReturn(transactions);
        when(accountManagementService.transfer(transactions)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = accountController.transfer(transferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(mapper, times(1)).transferTransactions(transferRequest);
        verify(accountManagementService, times(1)).transfer(transactions);
    }
}