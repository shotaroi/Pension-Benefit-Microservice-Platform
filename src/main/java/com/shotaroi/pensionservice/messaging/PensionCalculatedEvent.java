package com.shotaroi.pensionservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PensionCalculatedEvent {

    private Long citizenId;
    private BigDecimal estimatedMonthlyPension;
    private Integer retirementAge;
}
