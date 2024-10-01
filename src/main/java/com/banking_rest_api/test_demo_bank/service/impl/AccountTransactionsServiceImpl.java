package com.banking_rest_api.test_demo_bank.service.impl;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.repository.TransactionsRepository;
import com.banking_rest_api.test_demo_bank.service.AccountTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AccountTransactionsServiceImpl implements AccountTransactionsService {

    @Autowired
    private TransactionsRepository transRepo;

    @Override
    public List<Transaction> allTransactionsForAccountID(Long accountID) {
        return transRepo.findByAccount(accountID);
    }

    @Override
    public TransactionResponse saveTransaction(Transaction trans) {

        transRepo.save(trans);
        return TransactionResponse.builder()
                .wasSuccessful(true)
                .transactionID(trans.getOrderID())
                .build();
    }
}
