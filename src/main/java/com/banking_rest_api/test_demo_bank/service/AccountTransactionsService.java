package com.banking_rest_api.test_demo_bank.service;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;

import java.math.BigInteger;
import java.util.List;

public interface AccountTransactionsService {
    List<Transaction> allTransactionsForAccountID(Long accountID);
    TransactionResponse saveTransaction(Transaction trans);
}
