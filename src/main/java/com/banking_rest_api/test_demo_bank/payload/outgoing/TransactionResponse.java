package com.banking_rest_api.test_demo_bank.payload.outgoing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private boolean successful;
    private String transactionID;
    private String description;
}
