package com.shotaroi.pensionservice.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionResponse {

    private Long id;
    private Long citizenId;
    private Integer year;
    private BigDecimal salary;
    private BigDecimal contributionAmount;
    private LocalDateTime createdAt;
}
