package com.banking_rest_api.test_demo_bank.model.dtos;

import java.math.BigDecimal;
import java.util.Date;

public record AccountDTOWithoutTransactions(Long id, String firstName, String lastName, Date birthday, BigDecimal balance) {}
