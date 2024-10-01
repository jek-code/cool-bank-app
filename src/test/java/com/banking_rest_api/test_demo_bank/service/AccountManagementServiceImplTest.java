package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.service.impl.AccountManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountManagementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTransactionsService accountTransactionsService;

    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account();
        account.setFirst_name("John");
        AccountCreatedResponse expectedResponse = AccountCreatedResponse.builder()
                .wasSuccessful(true)
                .description("Account created")
                .build();

        when(accountRepository.save(account)).thenReturn(account);

        AccountCreatedResponse response = accountManagementService.saveAccount(account);

        assertEquals(expectedResponse, response);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testGetById_AccountFound() throws AccountNotFoundException {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setFirst_name("John");
        account.setLast_name("Ddddd");
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountTransactionsService.allTransactionsForAccountID(accountId)).thenReturn(List.of());

        AccountDTO accountDTO = accountManagementService.getById(accountId);

        assertNotNull(accountDTO);
        assertEquals(accountId, accountDTO.id());
        assertEquals("John", accountDTO.firstName());
        assertEquals("Ddddd", accountDTO.lastName());
        assertEquals(BigDecimal.valueOf(1000), accountDTO.balance());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void testGetById_AccountNotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountManagementService.getById(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void testGetAllUsers() {
        List<Account> accounts = List.of(new Account(), new Account());

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountManagementService.getAllUsers();

        assertEquals(accounts.size(), result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void testDeposit() {
        Long accountId = 1L;
        Transaction transaction = new Transaction();
        transaction.setAccount(accountId);
        transaction.setSum(BigDecimal.valueOf(500));
        transaction.setType(TransactionType.DEPOSIT);

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.getReferenceById(accountId)).thenReturn(account);
        when(accountTransactionsService.saveTransaction(transaction)).thenReturn(new TransactionResponse());

        TransactionResponse response = accountManagementService.deposit(transaction);

        assertNotNull(response);
        verify(accountRepository, times(1)).getReferenceById(accountId);
        verify(accountRepository, times(1)).save(account);
        verify(accountTransactionsService, times(1)).saveTransaction(transaction);
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    @Test
    public void testWithdraw_SufficientBalance() {
        Long accountId = 1L;
        Transaction transaction = new Transaction();
        transaction.setAccount(accountId);
        transaction.setSum(BigDecimal.valueOf(500));
        transaction.setType(TransactionType.WITHDRAW);

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.getReferenceById(accountId)).thenReturn(account);
        when(accountTransactionsService.saveTransaction(transaction)).thenReturn(new TransactionResponse());

        TransactionResponse response = accountManagementService.withdraw(transaction);

        assertNotNull(response);
        verify(accountRepository, times(1)).getReferenceById(accountId);
        verify(accountRepository, times(1)).save(account);
        verify(accountTransactionsService, times(1)).saveTransaction(transaction);
        assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    public void testWithdraw_InsufficientBalance() {
        Long accountId = 1L;
        Transaction transaction = new Transaction();
        transaction.setAccount(accountId);
        transaction.setSum(BigDecimal.valueOf(1500)); // More than the current balance
        transaction.setType(TransactionType.WITHDRAW);

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.getReferenceById(accountId)).thenReturn(account);

        TransactionResponse response = accountManagementService.withdraw(transaction);

        assertNull(response); // Expect null since balance is insufficient
        verify(accountRepository, times(1)).getReferenceById(accountId);
        verify(accountRepository, times(0)).save(account); // No saving should happen
        verify(accountTransactionsService, times(0)).saveTransaction(transaction); // No transaction saving
    }

    @Test
    public void testTransfer() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setAccount(senderAccountId);
        withdrawTransaction.setSum(BigDecimal.valueOf(300));
        withdrawTransaction.setType(TransactionType.OUTGOING_TRANSFER);

        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(receiverAccountId);
        depositTransaction.setSum(BigDecimal.valueOf(300));
        depositTransaction.setType(TransactionType.INCOMING_TRANSFER);

        List<Transaction> transactions = List.of(withdrawTransaction, depositTransaction);

        Account senderAccount = new Account();
        senderAccount.setId(senderAccountId);
        senderAccount.setBalance(BigDecimal.valueOf(1000));

        Account receiverAccount = new Account();
        receiverAccount.setId(receiverAccountId);
        receiverAccount.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.getReferenceById(senderAccountId)).thenReturn(senderAccount);
        when(accountRepository.getReferenceById(receiverAccountId)).thenReturn(receiverAccount);
        when(accountTransactionsService.saveTransaction(withdrawTransaction)).thenReturn(new TransactionResponse());
        when(accountTransactionsService.saveTransaction(depositTransaction)).thenReturn(new TransactionResponse());

        TransactionResponse response = accountManagementService.transfer(transactions);

        assertNotNull(response);
        verify(accountRepository, times(1)).getReferenceById(senderAccountId);
        verify(accountRepository, times(1)).getReferenceById(receiverAccountId);
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(accountTransactionsService, times(2)).saveTransaction(any(Transaction.class));

        assertEquals(BigDecimal.valueOf(700), senderAccount.getBalance());
        assertEquals(BigDecimal.valueOf(800), receiverAccount.getBalance());
    }
}