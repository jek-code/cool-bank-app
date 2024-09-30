package com.banking_rest_api.test_demo_bank.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AccountManagementServiceImplTest {

    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;

    @Mock
    private AccountRepository repo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account();
        account.setFirst_name("John");
        account.setLast_name("Doe");

        accountManagementService.saveAccount(account);

        verify(repo, times(1)).save(account);
    }

    @Test
    public void testGetById_WhenAccountExists() {
        int accountId = 1;
        Account account = new Account();
        account.setId(accountId);
        account.setFirst_name("John");
        account.setLast_name("Doe");

        when(repo.findById(accountId)).thenReturn(Optional.of(account));

        Account foundAccount = accountManagementService.getById(accountId);

        assertNotNull(foundAccount);
        assertEquals(accountId, foundAccount.getId());
        assertEquals("John", foundAccount.getFirst_name());
        verify(repo, times(1)).findById(accountId);
    }

    @Test
    public void testGetById_WhenAccountDoesNotExist() {
        int accountId = 1;

        when(repo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountManagementService.getById(accountId);
        });

        verify(repo, times(1)).findById(accountId);
    }

    @Test
    public void testGetAllUsers() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setFirst_name("John");
        account1.setLast_name("Doe");

        Account account2 = new Account();
        account2.setId(2);
        account2.setFirst_name("Jane");
        account2.setLast_name("Doe");

        List<Account> accounts = Arrays.asList(account1, account2);

        when(repo.findAll()).thenReturn(accounts);

        List<Account> foundAccounts = accountManagementService.getAllUsers();

        assertEquals(2, foundAccounts.size());
        assertEquals("John", foundAccounts.get(0).getFirst_name());
        assertEquals("Jane", foundAccounts.get(1).getFirst_name());
        verify(repo, times(1)).findAll();
    }
}