package com.banking_rest_api.test_demo_bank.mapper;

import com.banking_rest_api.test_demo_bank.model.Account;
import com.banking_rest_api.test_demo_bank.model.AccountDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO accountToDto(Account acc);
    Account dtoToAccount(AccountDTO accountDTO);

}
