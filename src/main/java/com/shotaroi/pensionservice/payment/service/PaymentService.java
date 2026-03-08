package com.shotaroi.pensionservice.payment.service;

import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.domain.PaymentStatus;
import com.shotaroi.pensionservice.domain.PensionPayment;
import com.shotaroi.pensionservice.exception.ResourceNotFoundException;
import com.shotaroi.pensionservice.payment.dto.PensionPaymentResponse;
import com.shotaroi.pensionservice.payment.mapper.PensionPaymentMapper;
import com.shotaroi.pensionservice.payment.repository.PensionPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PensionPaymentRepository paymentRepository;
    private final CitizenRepository citizenRepository;
    private final PensionPaymentMapper paymentMapper;

    @Transactional
    public PensionPaymentResponse generateFirstPayment(Long citizenId, BigDecimal amount) {
        if (!citizenRepository.existsById(citizenId)) {
            throw new ResourceNotFoundException("Citizen", citizenId);
        }

        LocalDate paymentDate = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        PensionPayment payment = PensionPayment.builder()
                .citizenId(citizenId)
                .amount(amount)
                .paymentDate(paymentDate)
                .status(PaymentStatus.PROCESSING)
                .createdAt(now)
                .updatedAt(now)
                .build();

        payment = paymentRepository.save(payment);

        // Simulate processing completion
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        log.info("Generated first pension payment for citizen {}: amount={}", citizenId, amount);
        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public List<PensionPaymentResponse> generatePaymentsForCitizen(Long citizenId) {
        if (!citizenRepository.existsById(citizenId)) {
            throw new ResourceNotFoundException("Citizen", citizenId);
        }

        // This would typically fetch pension amount from calculation - for now we use a placeholder
        // In real flow, this is triggered by PensionCalculatedEvent
        return getByCitizenId(citizenId);
    }

    @Transactional(readOnly = true)
    public List<PensionPaymentResponse> getByCitizenId(Long citizenId) {
        if (!citizenRepository.existsById(citizenId)) {
            throw new ResourceNotFoundException("Citizen", citizenId);
        }
        return paymentRepository.findByCitizenIdOrderByPaymentDateDesc(citizenId).stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
