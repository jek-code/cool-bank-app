package com.banking_rest_api.test_demo_bank.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public record AccountDTO (int id, String firstName, String LastName, Date birthday, BigInteger balance, List<Transaction> transactions) { }
