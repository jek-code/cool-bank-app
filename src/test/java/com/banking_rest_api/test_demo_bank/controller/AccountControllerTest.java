package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountManagementService accountManagementService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewAccount_Success() {
        Account account = new Account(); // Add necessary account fields here
        AccountCreatedResponse response = new AccountCreatedResponse(true, "Account Created Successfully");

        // Mock the service response
        when(accountManagementService.saveAccount(any(Account.class))).thenReturn(response);

        // Call the controller method
        ResponseEntity<AccountCreatedResponse> result = accountController.createNewAccount(account);

        // Verify response
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAccById_Success() {
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO(accountId, "John", "Doe", new Date(), BigDecimal.valueOf(1000.00), null);

        // Mock the service response
        when(accountManagementService.getAccountById(accountId)).thenReturn(accountDTO);

        // Call the controller method
        ResponseEntity<AccountDTO> result = accountController.getAccById(accountId);

        // Verify response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(accountDTO, result.getBody());
    }

    @Test
    void testGetAllAccounts_Success() {
        List<AccountDTOWithoutTransactions> accountDTOList = Arrays.asList(
                new AccountDTOWithoutTransactions(1L, "John", "Doe", new Date(), BigDecimal.valueOf(1000.00)),
                new AccountDTOWithoutTransactions(2L, "Jane", "Doe", new Date(), BigDecimal.valueOf(1500.00))
        );

        // Mock the service response
        when(accountManagementService.getAllAccounts()).thenReturn(accountDTOList);

        // Call the controller method
        ResponseEntity<List<AccountDTOWithoutTransactions>> result = accountController.getAllAccounts();

        // Verify response
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(accountDTOList, result.getBody());
    }
}