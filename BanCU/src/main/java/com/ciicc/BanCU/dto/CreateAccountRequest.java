package com.ciicc.BanCU.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank
    private String accountHolder;

    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal initialDeposit;
}