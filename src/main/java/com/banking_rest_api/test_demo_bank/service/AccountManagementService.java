package com.banking_rest_api.test_demo_bank.service;


import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;

import java.util.List;

public interface AccountManagementService {
    AccountCreatedResponse saveAccount(Account account);
    AccountDTO getAccountById(Long id);
    List<AccountDTOWithoutTransactions> getAllAccounts();

}
