package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class AccountController {

    @Autowired
    private AccountManagementService accountManagementService;
    @Autowired
    private RequestToTransactionMapper mapper;

    @PostMapping("/createUser")
    public ResponseEntity<AccountCreatedResponse> createNewAccount(@RequestBody Account account){
        return ResponseEntity.ok(accountManagementService.saveAccount(account));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountDTO> getAccById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountManagementService.getById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountManagementService.getAllUsers());
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(accountManagementService.deposit(mapper.depositTransaction(request)));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(accountManagementService.withdraw(mapper.withdrawTransaction(request)));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody ClientTransferRequest request) {
        return ResponseEntity.ok(accountManagementService.transfer(mapper.transferTransactions(request)));
    }
}
