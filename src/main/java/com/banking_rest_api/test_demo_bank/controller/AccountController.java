package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new user account", description = "This endpoint creates a new account for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping("/createUser")
    public ResponseEntity<AccountCreatedResponse> createNewAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountManagementService.saveAccount(account));
    }

    @Operation(summary = "Retrieve account by ID", description = "Fetches the account details based on the provided account ID. Contains details about User's transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found."),
            @ApiResponse(responseCode = "404", description = "Account not found.")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountDTO> getAccById(@Parameter(description = "ID of the account to be fetched") @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountManagementService.getById(id));
    }

    @Operation(summary = "Get all user accounts", description = "Retrieves a list of all user accounts.")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully.")
    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountManagementService.getAllUsers());
    }

    @Operation(summary = "Deposit money into an account", description = "Deposits a specified amount into the user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or account not found.")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(accountManagementService.deposit(mapper.depositTransaction(request)));
    }

    @Operation(summary = "Withdraw money from an account", description = "Withdraws a specified amount from the user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient funds.")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(accountManagementService.withdraw(mapper.withdrawTransaction(request)));
    }

    @Operation(summary = "Transfer money between accounts", description = "Transfers a specified amount from one account to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient funds.")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody ClientTransferRequest request) {
        return ResponseEntity.ok(accountManagementService.transfer(mapper.transferTransactions(request)));
    }
}