package com.banking_rest_api.test_demo_bank;

import com.banking_rest_api.test_demo_bank.controller.AccountController;
import com.banking_rest_api.test_demo_bank.service.AccountManagementService;
import com.banking_rest_api.test_demo_bank.service.AccountTransactionsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestDemoBankApplicationTests {

	@Autowired
	private AccountController accountController;

	@Autowired
	private AccountManagementService accountManagementService;

	@Autowired
	private AccountTransactionsService accountTransactionsService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(accountController, "AccountController should be loaded");
		Assertions.assertNotNull(accountManagementService, "AccountManagementService should be loaded");
		Assertions.assertNotNull(accountTransactionsService, "AccountTransactionsService should be loaded");
	}
}