package com.banking_rest_api.test_demo_bank.service;


import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;

import java.util.List;

public interface AccountManagementService {
    AccountCreatedResponse saveAccount(Account account);
    AccountDTO getById(Long id);
    List<Account> getAllUsers();
    TransactionResponse deposit(Transaction transaction);
    TransactionResponse withdraw(Transaction transaction);
    TransactionResponse transfer(List<Transaction> transactions);

}
