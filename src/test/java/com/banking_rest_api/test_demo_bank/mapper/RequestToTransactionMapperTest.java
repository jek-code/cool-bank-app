package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestToTransactionMapperTest {

    private RequestToTransactionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RequestToTransactionMapper();
    }

    @Test
    void testDepositTransaction() {
        ClientRequest request = new ClientRequest();
        request.setAccountID(1L);
        request.setSum(BigDecimal.valueOf(100.00));

        Transaction transaction = mapper.depositTransaction(request);

        assertEquals(1L, transaction.getAccountId());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(BigDecimal.valueOf(100.00), transaction.getSum());
        assertEquals(36, transaction.getOrderID().length()); // UUID has a length of 36
    }

    @Test
    void testWithdrawTransaction() {
        ClientRequest request = new ClientRequest();
        request.setAccountID(2L);
        request.setSum(BigDecimal.valueOf(50.00));

        Transaction transaction = mapper.withdrawTransaction(request);

        assertEquals(2L, transaction.getAccountId());
        assertEquals(TransactionType.WITHDRAW, transaction.getType());
        assertEquals(BigDecimal.valueOf(50.00), transaction.getSum());
        assertEquals(36, transaction.getOrderID().length()); // UUID has a length of 36
    }

    @Test
    void testTransferTransactions() {
        ClientTransferRequest request = new ClientTransferRequest();
        request.setSenderAccountID(1L);
        request.setReceiverAccountID(2L);
        request.setSum(BigDecimal.valueOf(75.00));

        List<Transaction> transactions = mapper.transferTransactions(request);

        assertEquals(2, transactions.size());

        // Withdraw transaction
        Transaction withdrawTransaction = transactions.get(0);
        assertEquals(1L, withdrawTransaction.getAccountId());
        assertEquals(TransactionType.OUTGOING_TRANSFER, withdrawTransaction.getType());
        assertEquals(BigDecimal.valueOf(75.00), withdrawTransaction.getSum());
        assertEquals(transactions.get(1).getOrderID(), withdrawTransaction.getOrderID()); // Same order ID

        // Deposit transaction
        Transaction depositTransaction = transactions.get(1);
        assertEquals(2L, depositTransaction.getAccountId());
        assertEquals(TransactionType.INCOMING_TRANSFER, depositTransaction.getType());
        assertEquals(BigDecimal.valueOf(75.00), depositTransaction.getSum());
        assertEquals(transactions.get(0).getOrderID(), depositTransaction.getOrderID()); // Same order ID
    }
}
