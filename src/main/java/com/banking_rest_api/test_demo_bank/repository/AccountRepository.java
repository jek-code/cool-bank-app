package com.banking_rest_api.test_demo_bank.repository;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    @EntityGraph(attributePaths = {"transactions"})
    Optional<Account> findWithTransactionsById(Long id);

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findWithoutTransactionsById(@Param("id") Long id);

    @Query("SELECT new com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions(a.id, a.firstName, a.lastName, a.birthday, a.balance) FROM Account a")
    List<AccountDTOWithoutTransactions> findAllAccountsWithoutTransactions();
}
