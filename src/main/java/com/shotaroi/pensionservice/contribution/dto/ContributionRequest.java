package com.shotaroi.pensionservice.contribution.dto;

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
public class ContributionRequest {

    @NotNull(message = "Citizen ID is required")
    private Long citizenId;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0", inclusive = false, message = "Salary must be positive")
    private BigDecimal salary;
}
