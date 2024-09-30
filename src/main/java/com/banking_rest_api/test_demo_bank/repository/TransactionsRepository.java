package com.banking_rest_api.test_demo_bank.repository;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccount(int accountID);
}
