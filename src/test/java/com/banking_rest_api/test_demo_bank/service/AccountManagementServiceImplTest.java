package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
import com.banking_rest_api.test_demo_bank.mapper.AccountMapper;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.service.impl.AccountManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class AccountManagementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAccount_Success() {
        Account account = new Account();
        account.setFirstName("John");

        // Call the service method
        AccountCreatedResponse response = accountManagementService.saveAccount(account);

        // Verify the response and interactions
        verify(accountRepository).save(account);
        assertTrue(response.isWasSuccessful());
        assertEquals("Account created", response.getDescription());
    }

    @Test
    void testGetAccountById_Success() {
        Long accountId = 1L;
        Account account = new Account();
        AccountDTO accountDTO = new AccountDTO(1L, "Nick", "Cave", new Date(), BigDecimal.valueOf(100.00), null);

        // Mock repository and mapper behavior
        when(accountRepository.findWithTransactionsById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.accountToDto(account)).thenReturn(accountDTO);

        // Call the service method
        AccountDTO result = accountManagementService.getAccountById(accountId);

        // Verify the response
        assertEquals(accountDTO, result);
    }

    @Test
    void testGetAccountById_NotFound() {
        Long accountId = 1L;

        // Mock repository behavior to return empty
        when(accountRepository.findWithTransactionsById(accountId)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountManagementService.getAccountById(accountId);
        });

        // Verify exception message
        assertEquals("Account with ID 1 not found", exception.getMessage());
    }

    @Test
    void testGetAllAccounts_Success() {
        List<AccountDTOWithoutTransactions> accountList = List.of(new AccountDTOWithoutTransactions(1L, "Nick", "Cave", new Date(), BigDecimal.valueOf(100.00)));

        // Mock repository behavior
        when(accountRepository.findAllAccountsWithoutTransactions()).thenReturn(accountList);

        // Call the service method
        List<AccountDTOWithoutTransactions> result = accountManagementService.getAllAccounts();

        // Verify the result
        assertEquals(accountList, result);
    }
}