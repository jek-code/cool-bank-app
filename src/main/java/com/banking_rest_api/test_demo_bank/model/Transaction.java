package com.banking_rest_api.test_demo_bank.model;

import com.banking_rest_api.test_demo_bank.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime createdAt;

    @Column(name = "account_id")
    private Long account;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "order_id")
    private String orderID;

}
