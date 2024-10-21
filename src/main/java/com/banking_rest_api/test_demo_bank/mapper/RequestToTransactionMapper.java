package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RequestToTransactionMapper {

    public Transaction depositTransaction(ClientRequest request) {
        return Transaction.builder()
                .accountId(request.getAccountID())
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .orderId(UUID.randomUUID().toString())
                .build();
    }

    public Transaction withdrawTransaction(ClientRequest request) {
        return Transaction.builder()
                .accountId(request.getAccountID())
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .orderId(UUID.randomUUID().toString())
                .build();
    }

    public List<Transaction> transferTransactions(ClientTransferRequest request) {

        List<Transaction> result = new ArrayList<>();
        String orderID = UUID.randomUUID().toString();

        var withdrawTrans = Transaction.builder()
                .accountId(request.getSenderAccountID())
                .type(TransactionType.OUTGOING_TRANSFER)
                .amount(request.getAmount())
                .orderId(orderID)
                .build();

        var depTrans = Transaction.builder()
                .accountId(request.getReceiverAccountID())
                .type(TransactionType.INCOMING_TRANSFER)
                .amount(request.getAmount())
                .orderId(orderID)
                .build();

        result.add(withdrawTrans);
        result.add(depTrans);

        return result;
    }

}
