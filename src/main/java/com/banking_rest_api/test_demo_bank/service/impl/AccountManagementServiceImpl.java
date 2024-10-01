package com.banking_rest_api.test_demo_bank.service.impl;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import com.banking_rest_api.test_demo_bank.service.AccountTransactionsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Slf4j
@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    @Autowired
    private AccountRepository repo;
    @Autowired
    private AccountTransactionsService transServ;

    @Override
    public AccountCreatedResponse saveAccount(Account account) {
        log.info("creating new account for {}", account.getFirst_name());

        repo.save(account);
        return AccountCreatedResponse.builder()
                .wasSuccessful(true)
                .description("Account created")
                .build();
    }


    @Override
    @SneakyThrows
    public AccountDTO getById(Long id) {
        log.info("searching for account #{}", id);

        var acc =  repo.findById(id).orElseThrow(AccountNotFoundException::new);
        return new AccountDTO(acc.getId(), acc.getFirst_name(), acc.getLast_name(), acc.getBirthday(), acc.getBalance(), transServ.allTransactionsForAccountID(id));
    }

    @Override
    public List<Account> getAllUsers() {
        return repo.findAll();
    }

    @Transactional
    public TransactionResponse deposit(Transaction transaction) {
        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        var acc = repo.getReferenceById(transaction.getAccount());
        acc.setBalance(acc.getBalance().add(transaction.getSum()));
        repo.save(acc);

        return transServ.saveTransaction(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(Transaction transaction) {
        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        var account = repo.getReferenceById(transaction.getAccount());
        var sum = transaction.getSum();
        var currentBalance = account.getBalance();

        if (sum.compareTo(currentBalance) > 0) return null;

        account.setBalance(currentBalance.subtract(sum));
        repo.save(account);

        return transServ.saveTransaction(transaction);
    }

    @Transactional
    public TransactionResponse transfer(List<Transaction> transactions) {
        withdraw(transactions.get(0));
        return deposit(transactions.get(1));
    }
}
