package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountTransactionsServiceImpl implements AccountTransactionsService {

    @Autowired
    private AccountManagementService managementService;
    @Autowired
    private TransactionsRepository transRepo;

    @Override
    public List<Transaction> allTransactionsForAccountID(int accountID) {
        return transRepo.findByAccount(accountID);
    }

    @Override
    public TransactionResponse deposit(Transaction transaction) {

        var account = managementService.getById(transaction.getAccount());

        account.setBalance(account.getBalance().add(transaction.getSum()));
        transaction.setCreatedOn(new Date());

        managementService.saveAccount(account);
        transRepo.save(transaction);

        return TransactionResponse.builder()
                .transactionID(transaction.getOrderID())
                .wasSuccessful(true)
                .description( transaction.getSum() + "$ was sent to " + account.getFirst_name() + " " + account.getLast_name())
                .build();
    }


    @Override
    public TransactionResponse withdraw(Transaction transaction) {

        var account = managementService.getById(transaction.getAccount());
        var sum = transaction.getSum();
        var currentBalance = account.getBalance();

        if (sum.compareTo(currentBalance) > 0) return null;

        account.setBalance(currentBalance.subtract(sum));
        transaction.setCreatedOn(new Date());

        managementService.saveAccount(account);
        transRepo.save(transaction);

        return TransactionResponse.builder()
                .transactionID(transaction.getOrderID())
                .wasSuccessful(true)
                .description( transaction.getSum() + "$ withdrawn from account of " + account.getFirst_name() + " " + account.getLast_name())
                .build();
    }
}
