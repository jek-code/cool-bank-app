package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.dtos.AccountDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void testAccountToDto() {
        // Given
        Account account = Account.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthday(new Date())
                .balance(BigDecimal.valueOf(1000.00))
                .transactions(Arrays.asList()) // Assuming no transactions for simplicity
                .build();

        // When
        AccountDTO accountDTO = mapper.accountToDto(account);

        // Then
        assertEquals(account.getId(), accountDTO.id());
        assertEquals(account.getFirstName(), accountDTO.firstName());
        assertEquals(account.getLastName(), accountDTO.lastName());
        assertEquals(account.getBirthday(), accountDTO.birthday());
        assertEquals(account.getBalance(), accountDTO.balance());
        assertEquals(account.getTransactions(), accountDTO.transactions());
    }

    @Test
    void testDtoToAccount() {
        // Given
        AccountDTO accountDTO = new AccountDTO(
                2L,
                "Jane",
                "Doe",
                new Date(),
                BigDecimal.valueOf(2000.00),
                Arrays.asList() // Assuming no transactions for simplicity
        );

        // When
        Account account = mapper.dtoToAccount(accountDTO);

        // Then
        assertEquals(accountDTO.id(), account.getId());
        assertEquals(accountDTO.firstName(), account.getFirstName());
        assertEquals(accountDTO.lastName(), account.getLastName());
        assertEquals(accountDTO.birthday(), account.getBirthday());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.transactions(), account.getTransactions());
    }
}