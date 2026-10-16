package com.ciicc.BanCU.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MoneyRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
}