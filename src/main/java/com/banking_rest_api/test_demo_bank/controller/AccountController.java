package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.incoming.DepositRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.TransferRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.WithdrawRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import com.banking_rest_api.test_demo_bank.service.AccountTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AccountController {

    @Autowired
    private AccountManagementService managementService;

    @Autowired
    private AccountTransactionsService transactionsService;

    @PostMapping("/createUser")
    public ResponseEntity<AccountCreatedResponse> createNewAccount(@RequestBody Account account){
        managementService.saveAccount(account);
        return ResponseEntity.ok(AccountCreatedResponse.builder()
                        .wasSuccessful(true)
                        .description("Account created")
                        .build());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountDTO> getAccById(@PathVariable("id") int id) {
        var acc = managementService.getById(id);
        return new ResponseEntity<>(new AccountDTO(acc.getId(), acc.getFirst_name(), acc.getLast_name(), acc.getBirthday(), acc.getBalance(), transactionsService.allTransactionsForAccountID(id)), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(managementService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositRequest request) {

        transactionsService.deposit(Transaction.builder()
                            .account(request.getAccountId())
                            .type(TransactionType.DEPOSIT)
                            .sum(request.getSum())
                            .orderID(UUID.randomUUID().toString())
                            .build());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody WithdrawRequest request) {

        transactionsService.withdraw(Transaction.builder()
                            .account(request.getAccountId())
                            .type(TransactionType.WITHDRAW)
                            .sum(request.getSum())
                            .orderID(UUID.randomUUID().toString())
                            .build());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request) {

        var orderID = UUID.randomUUID().toString();

        transactionsService.withdraw(Transaction.builder()
                .account(request.getSenderAccountID())
                .type(TransactionType.OUTGOING_TRANSFER)
                .sum(request.getSum())
                .orderID(orderID)
                .build());

        transactionsService.deposit(Transaction.builder()
                .account(request.getReceiverAccountID())
                .type(TransactionType.INCOMING_TRANSFER)
                .sum(request.getSum())
                .orderID(orderID)
                .build());


        return new ResponseEntity<>(HttpStatus.OK);
    }

}
