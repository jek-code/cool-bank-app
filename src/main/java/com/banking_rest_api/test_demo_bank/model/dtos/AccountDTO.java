package com.banking_rest_api.test_demo_bank.model.dtos;

import com.banking_rest_api.test_demo_bank.model.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public record AccountDTO (Long id, String firstName, String lastName, Date birthday, BigDecimal balance, List<Transaction> transactions) { }
