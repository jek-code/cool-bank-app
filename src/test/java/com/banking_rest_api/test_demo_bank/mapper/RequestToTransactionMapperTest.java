package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.mapper.RequestToTransactionMapper;
import com.banking_rest_api.test_demo_bank.model.Transaction;
import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientRequest;
import com.banking_rest_api.test_demo_bank.payload.incoming.ClientTransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestToTransactionMapperTest {

    private RequestToTransactionMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new RequestToTransactionMapper();
    }

    @Test
    public void testDepositTransaction() {
        ClientRequest request = new ClientRequest(1L, BigDecimal.valueOf(1000));

        Transaction transaction = mapper.depositTransaction(request);

        assertNotNull(transaction);
        assertEquals(request.getAccountID(), transaction.getAccount());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(request.getSum(), transaction.getSum());
        assertNotNull(transaction.getOrderID());
    }

    @Test
    public void testWithdrawTransaction() {
        ClientRequest request = new ClientRequest(1L, BigDecimal.valueOf(500));

        Transaction transaction = mapper.withdrawTransaction(request);

        assertNotNull(transaction);
        assertEquals(request.getAccountID(), transaction.getAccount());
        assertEquals(TransactionType.WITHDRAW, transaction.getType());
        assertEquals(request.getSum(), transaction.getSum());
        assertNotNull(transaction.getOrderID());
    }

    @Test
    public void testTransferTransactions() {
        ClientTransferRequest request = new ClientTransferRequest(1L, 2L, BigDecimal.valueOf(300));

        List<Transaction> transactions = mapper.transferTransactions(request);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        Transaction withdrawTrans = transactions.get(0);
        assertEquals(request.getSenderAccountID(), withdrawTrans.getAccount());
        assertEquals(TransactionType.OUTGOING_TRANSFER, withdrawTrans.getType());
        assertEquals(request.getSum(), withdrawTrans.getSum());
        assertNotNull(withdrawTrans.getOrderID());

        Transaction depTrans = transactions.get(1);
        assertEquals(request.getReceiverAccountID(), depTrans.getAccount());
        assertEquals(TransactionType.INCOMING_TRANSFER, depTrans.getType());
        assertEquals(request.getSum(), depTrans.getSum());
        assertEquals(withdrawTrans.getOrderID(), depTrans.getOrderID());
    }
}