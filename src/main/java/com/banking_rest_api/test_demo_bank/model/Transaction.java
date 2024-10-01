package com.banking_rest_api.test_demo_bank.model;

import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long account;

    @Column(name = "date_created")
    private Date createdOn;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "order_id")
    private String orderID;

}
