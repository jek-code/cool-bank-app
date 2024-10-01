package com.banking_rest_api.test_demo_bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import com.banking_rest_api.test_demo_bank.service.AccountTransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountManagementService managementService;

    @Mock
    private AccountTransactionsService transactionsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testCreateNewAccount() throws Exception {

        var account = Account.builder()
                .first_name("Sarah")
                .last_name("Doe")
                .balance(BigInteger.valueOf(500))
                .build();


        mockMvc.perform(post("/api/v1/createUser")
                        .contentType("application/json")
                        .content("{\"first_name\":\"Sarah\", \"last_name\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wasSuccessful").value(true))
                .andExpect(jsonPath("$.description").value("Account created"));

        verify(managementService, times(1)).saveAccount(any(Account.class));
    }

    @Test
    public void testGetAccById() throws Exception {

        Long accountId = 1L;
        var account = Account.builder()
                .id(1L)
                .first_name("Sarah")
                .last_name("Doe")
                .balance(BigDecimal.valueOf(500.0))
                .build();

        when(managementService.getById(accountId)).thenReturn(account);
        when(transactionsService.allTransactionsForAccountID(accountId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/getById/{id}", accountId))
                .andExpect(status().isOk());

        verify(managementService, times(1)).getById(accountId);
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        List<Account> accounts = Arrays.asList(new Account(), new Account());

        when(managementService.getAllUsers()).thenReturn(accounts);

        mockMvc.perform(get("/api/v1/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(managementService, times(1)).getAllUsers();
    }

    @Test
    public void testDeposit() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setAccountId(1L);
        request.setSum(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/deposit")
                        .contentType("application/json")
                        .content("{\"accountId\":1, \"sum\":100.0}"))
                .andExpect(status().isOk());

        verify(transactionsService, times(1)).deposit(any(Transaction.class));
    }

    @Test
    public void testWithdraw() throws Exception {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountId(1L);
        request.setSum(BigDecimal.valueOf(50));

        mockMvc.perform(post("/api/v1/withdraw")
                        .contentType("application/json")
                        .content("{\"accountId\":1, \"sum\":50.0}"))
                .andExpect(status().isOk());

        verify(transactionsService, times(1)).withdraw(any(Transaction.class));
    }

    @Test
    public void testTransfer() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setSenderAccountID(1L);
        request.setReceiverAccountID(2L);
        request.setSum(BigDecimal.valueOf(75));

        mockMvc.perform(post("/api/v1/transfer")
                        .contentType("application/json")
                        .content("{\"senderAccountID\":1, \"receiverAccountID\":2, \"sum\":75.0}"))
                .andExpect(status().isOk());

        verify(transactionsService, times(1)).withdraw(any(Transaction.class));
        verify(transactionsService, times(1)).deposit(any(Transaction.class));
    }
}