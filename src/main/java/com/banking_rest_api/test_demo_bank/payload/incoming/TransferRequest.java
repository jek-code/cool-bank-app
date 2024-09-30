package com.banking_rest_api.test_demo_bank.payload.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private int receiverAccountID;
    private int senderAccountID;
    private BigInteger sum;

}
