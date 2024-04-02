package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties({"email", "firstName", "lastName", "password", "status", "roles", "hibernateLazyInitializer"})
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private Double amount;

    private LocalDate date;

    private String currency;

    @OneToOne
    private TransactionType transactionType;
}