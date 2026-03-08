package com.shotaroi.pensionservice.pension.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PensionCalculationRequest {

    @NotNull(message = "Citizen ID is required")
    private Long citizenId;

    @NotNull(message = "Retirement age is required")
    @Min(value = 60, message = "Retirement age must be at least 60")
    @Max(value = 75, message = "Retirement age must be at most 75")
    private Integer retirementAge;
}
