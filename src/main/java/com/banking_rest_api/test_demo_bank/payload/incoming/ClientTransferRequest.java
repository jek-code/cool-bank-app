package com.banking_rest_api.test_demo_bank.payload.incoming;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Sender account ID is required")
    private Long senderAccountID;

    @NotNull(message = "Receiver account ID is required")
    private Long receiverAccountID;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;
}
