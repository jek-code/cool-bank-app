package com.banking_rest_api.test_demo_bank.payload.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTransferRequest {
    private Long senderAccountID;
    private Long receiverAccountID;
    private BigDecimal sum;
}
