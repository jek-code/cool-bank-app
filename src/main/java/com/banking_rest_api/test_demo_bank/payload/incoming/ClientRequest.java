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
public class ClientRequest {

    private Long accountID;
    private BigDecimal sum;
}
