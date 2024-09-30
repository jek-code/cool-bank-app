package com.banking_rest_api.test_demo_bank.service;


import com.banking_rest_api.test_demo_bank.model.Account;

import java.util.List;

public interface AccountManagementService {
    void saveAccount(Account account);
    Account getById(int id);
    List<Account> getAllUsers();

}
