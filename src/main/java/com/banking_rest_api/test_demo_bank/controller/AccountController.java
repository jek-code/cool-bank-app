package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import com.banking_rest_api.test_demo_bank.payload.outgoing.AccountCreatedResponse;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class AccountController {

    private final AccountManagementService accountManagementService;

    @Operation(summary = "Create a new user account", description = "This endpoint creates a new account for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping("/createAccount")
    public ResponseEntity<AccountCreatedResponse> createNewAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountManagementService.saveAccount(account));
    }

    @Operation(summary = "Retrieve account by ID", description = "Fetches the account details based on the provided account ID. Contains details about User's transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found."),
            @ApiResponse(responseCode = "404", description = "Account not found.")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountDTO> getAccById(@Valid @Parameter(description = "ID of the account to be fetched") @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountManagementService.getAccountById(id));
    }

    @Operation(summary = "Get all user accounts", description = "Retrieves a list of all user accounts.")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully.")
    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountManagementService.getAllAccounts());
    }
}