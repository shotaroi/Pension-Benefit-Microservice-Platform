package com.shotaroi.pensionservice.payment.dto;

import com.shotaroi.pensionservice.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PensionPaymentResponse {

    private Long id;
    private Long citizenId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
