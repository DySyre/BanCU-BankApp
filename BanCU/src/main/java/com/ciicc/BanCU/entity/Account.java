package com.ciicc.BanCU.entity;

import com.ciicc.BanCU.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "account_number",
        nullable = false,
        unique = true,
        length = 20
    )
    private String accountNumber;

    @Column(name = "account_holder", nullable = false)
    private String accountHolder;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;
}