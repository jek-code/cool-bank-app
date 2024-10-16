package com.banking_rest_api.test_demo_bank.controller;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import com.banking_rest_api.test_demo_bank.payload.outgoing.TransactionResponse;
import com.banking_rest_api.test_demo_bank.service.TransactionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class TransactionsController {

    private final TransactionsService transactionsService;
    private final RequestToTransactionMapper mapper;

    @Operation(summary = "Deposit money into an account", description = "Deposits a specified amount into the user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or account not found.")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(transactionsService.deposit(mapper.depositTransaction(request)));
    }

    @Operation(summary = "Withdraw money from an account", description = "Withdraws a specified amount from the user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient funds.")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(transactionsService.withdraw(mapper.withdrawTransaction(request)));
    }

    @Operation(summary = "Transfer money between accounts", description = "Transfers a specified amount from one account to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient funds.")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody ClientTransferRequest request) {
        return ResponseEntity.ok(transactionsService.transfer(mapper.transferTransactions(request)));
    }
}
