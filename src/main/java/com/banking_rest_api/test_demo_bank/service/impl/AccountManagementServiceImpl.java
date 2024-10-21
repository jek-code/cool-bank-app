package com.banking_rest_api.test_demo_bank.service.impl;

import com.banking_rest_api.test_demo_bank.exception.AccountNotFoundException;
import com.banking_rest_api.test_demo_bank.mapper.AccountMapper;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTOWithoutTransactions;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.repository.AccountRepository;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {

    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    @Override
    public AccountCreatedResponse saveAccount(Account account) {
        log.info("creating new account for {}", account.getFirstName());

        accountRepository.save(account);
        return AccountCreatedResponse.builder()
                .wasSuccessful(true)
                .description("Account created")
                .build();
    }


    @Override
    public AccountDTO getAccountById(Long id) {
        log.info("searching for account #{}", id);
        return mapper.accountToDto(accountRepository.findWithTransactionsById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found")));
    }

    @Override
    public List<AccountDTOWithoutTransactions> getAllAccounts() {
        return accountRepository.findAllAccountsWithoutTransactions();
    }
}
