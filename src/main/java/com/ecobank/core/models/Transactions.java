
package com.ecobank.core.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;   // assumes you have com.ecobank.core.models.Account

    @NotNull
    private Double amount;     // positive for credit, negative for debit

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank
    private String description;

    }
