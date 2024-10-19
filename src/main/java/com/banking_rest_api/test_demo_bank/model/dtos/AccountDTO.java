package com.banking_rest_api.test_demo_bank.model.dtos;

import com.banking_rest_api.test_demo_bank.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record AccountDTO (Long id, String first_name, String last_name, Date birthday, BigDecimal balance, List<Transaction> transactions) { }
