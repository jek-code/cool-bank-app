package com.banking_rest_api.test_demo_bank.payload.incoming;


import com.banking_rest_api.test_demo_bank.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {

    private int accountId;
    private BigInteger sum;

}
