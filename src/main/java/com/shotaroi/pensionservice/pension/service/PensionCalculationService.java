package com.shotaroi.pensionservice.pension.service;

import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.config.JmsConfig;
import com.shotaroi.pensionservice.contribution.service.ContributionService;
import com.shotaroi.pensionservice.exception.ResourceNotFoundException;
import com.shotaroi.pensionservice.messaging.PensionCalculatedEvent;
import com.shotaroi.pensionservice.pension.dto.PensionCalculationRequest;
import com.shotaroi.pensionservice.pension.dto.PensionCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PensionCalculationService {

    /**
     * Simplified pension formula: monthlyPension = totalContributions / 240
     * (Assumes ~20 years of pension payments: 20 * 12 = 240 months)
     */
    private static final int PENSION_DIVISOR = 240;

    private final ContributionService contributionService;
    private final CitizenRepository citizenRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional(readOnly = true)
    public PensionCalculationResponse calculate(PensionCalculationRequest request) {
        if (!citizenRepository.existsById(request.getCitizenId())) {
            throw new ResourceNotFoundException("Citizen", request.getCitizenId());
        }

        BigDecimal totalContributions = contributionService.getTotalContributionsByCitizenId(request.getCitizenId());
        BigDecimal estimatedMonthlyPension = totalContributions
                .divide(BigDecimal.valueOf(PENSION_DIVISOR), 2, RoundingMode.HALF_UP);

        PensionCalculationResponse response = PensionCalculationResponse.builder()
                .citizenId(request.getCitizenId())
                .estimatedMonthlyPension(estimatedMonthlyPension)
                .retirementAge(request.getRetirementAge())
                .build();

        // Publish event for Payment Service to generate first payment
        publishPensionCalculatedEvent(response);

        return response;
    }

    private void publishPensionCalculatedEvent(PensionCalculationResponse response) {
        PensionCalculatedEvent event = PensionCalculatedEvent.builder()
                .citizenId(response.getCitizenId())
                .estimatedMonthlyPension(response.getEstimatedMonthlyPension())
                .retirementAge(response.getRetirementAge())
                .build();
        jmsTemplate.convertAndSend(JmsConfig.PENSION_CALCULATED_QUEUE, event);
    }
}
