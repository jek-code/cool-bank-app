package com.banking_rest_api.test_demo_bank.service.impl;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
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
        return transactionsRepository.findByAccountId(accountID);
    }

    @Override
    @Transactional
    public TransactionResponse deposit(Transaction transaction) {

        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        var accId = transaction.getAccountId();
        Account account = accountRepository.findWithoutTransactionsById(accId).orElseThrow(() -> new AccountNotFoundException("Account with ID " + accId + " not found"));
        account.setBalance(account.getBalance().add(transaction.getSum()));
        accountRepository.save(account);
        transactionsRepository.save(transaction);

        return TransactionResponse.builder()
                .successful(true)
                .transactionID(transaction.getOrderID())
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Transaction transaction) {

        log.info("starting {} order {}", transaction.getType().toString(),transaction.getOrderID());

        var accId = transaction.getAccountId();
        Account account = accountRepository.findWithoutTransactionsById(accId).orElseThrow(() -> new AccountNotFoundException("Account with ID " + accId + " not found"));

        if (account.getBalance().compareTo(transaction.getSum()) >= 0) {

            account.setBalance(account.getBalance().subtract(transaction.getSum()));
            accountRepository.save(account);
            transactionsRepository.save(transaction);

            return TransactionResponse.builder()
                    .successful(true)
                    .transactionID(transaction.getOrderID())
                    .build();
        }


        return TransactionResponse.builder()
                .successful(false)
                .description("Insufficient funds")
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse transfer(List<Transaction> transactions) {

        var withdrawResponse = withdraw(transactions.get(0));

        if (!withdrawResponse.isSuccessful())
            return withdrawResponse;

        return deposit(transactions.get(1));
    }
}
