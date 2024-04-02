package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"email", "firstName", "lastName", "password", "status", "roles"})
    private User user;

    @Enumerated(EnumType.STRING)
    private Month month;

    private int year;

    private Double balance;

    private Double budget;

    private String currency;
}