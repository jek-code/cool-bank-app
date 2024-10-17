package com.banking_rest_api.test_demo_bank.repository;

import com.banking_rest_api.test_demo_bank.model.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Fetch account with transactions for fetching by ID
    @EntityGraph(attributePaths = {"transactions"})
    Optional<Account> findByIdWithTransactions(Long id);

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdWithoutTransactions(@Param("id") Long id);
    // Fetch all accounts without transactions
    @Query("SELECT a FROM Account a")
    List<Account> findAllAccountsWithoutTransactions();
}
