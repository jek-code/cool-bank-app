package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    @Autowired
    private AccountRepository repo;

    @Override
    public void saveAccount(Account account) {
        repo.save(account);
    }

    @Override
    public Account getById(int id) {
        return repo.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public List<Account> getAllUsers() {
        return repo.findAll();
    }

}
