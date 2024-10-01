package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RequestToTransactionMapper {

    public Transaction depositTransaction(ClientRequest request) {
        return Transaction.builder()
                .account(request.getAccountID())
                .createdOn(new Date())
                .type(TransactionType.DEPOSIT)
                .sum(request.getSum())
                .orderID(UUID.randomUUID().toString())
                .build();
    }

    public Transaction withdrawTransaction(ClientRequest request) {
        return Transaction.builder()
                .account(request.getAccountID())
                .createdOn(new Date())
                .type(TransactionType.WITHDRAW)
                .sum(request.getSum())
                .orderID(UUID.randomUUID().toString())
                .build();
    }

    public List<Transaction> transferTransactions(ClientTransferRequest request) {

        List<Transaction> result = new ArrayList<>();
        String orderID = UUID.randomUUID().toString();

        var withdrawTrans = Transaction.builder()
                .account(request.getSenderAccountID())
                .createdOn(new Date())
                .type(TransactionType.OUTGOING_TRANSFER)
                .sum(request.getSum())
                .orderID(orderID)
                .build();

        var depTrans = Transaction.builder()
                .account(request.getReceiverAccountID())
                .createdOn(new Date())
                .type(TransactionType.INCOMING_TRANSFER)
                .sum(request.getSum())
                .orderID(orderID)
                .build();

        result.add(withdrawTrans);
        result.add(depTrans);

        return result;
    }

}
