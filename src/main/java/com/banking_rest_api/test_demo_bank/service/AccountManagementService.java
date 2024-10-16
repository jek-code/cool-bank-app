package com.banking_rest_api.test_demo_bank.service;


import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;

import java.util.List;

public interface AccountManagementService {
    AccountCreatedResponse saveAccount(Account account);
    AccountDTO getAccountById(Long id);
    List<Account> getAllAccounts();

}
