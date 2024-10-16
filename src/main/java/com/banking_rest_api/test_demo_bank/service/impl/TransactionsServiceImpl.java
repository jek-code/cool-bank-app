package com.banking_rest_api.test_demo_bank.service.impl;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import com.banking_rest_api.test_demo_bank.service.TransactionsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<Transaction> allTransactionsForAccountID(Long accountID) {
        return transactionsRepository.findByAccount(accountID);
    }

    @Override
    public TransactionResponse saveTransaction(Transaction trans) {

        transactionsRepository.save(trans);
        return TransactionResponse.builder()
                .wasSuccessful(true)
                .transactionID(trans.getOrderID())
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse deposit(Transaction transaction) {
        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        Account account = accountRepository.findById(transaction.getAccount()).orElseThrow();
        account.setBalance(account.getBalance().add(transaction.getSum()));
        accountRepository.save(account);
        transactionsRepository.save(transaction);
        return TransactionResponse.builder()
                .wasSuccessful(true)
                .transactionID(transaction.getOrderID())
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Transaction transaction) {
        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        Account account = accountRepository.findById(transaction.getAccount()).orElseThrow();

        if (account.getBalance().compareTo(transaction.getSum()) >= 0) {
            account.setBalance(account.getBalance().subtract(transaction.getSum()));
            accountRepository.save(account);
            transactionsRepository.save(transaction);
            return TransactionResponse.builder()
                    .wasSuccessful(true)
                    .transactionID(transaction.getOrderID())
                    .build();
        }
        return TransactionResponse.builder()
                .wasSuccessful(false)
                .description("Insufficient funds")
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse transfer(List<Transaction> transactions) {
        withdraw(transactions.get(0));
        return deposit(transactions.get(1));
    }
}
