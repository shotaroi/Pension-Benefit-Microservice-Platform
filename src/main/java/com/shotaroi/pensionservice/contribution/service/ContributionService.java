package com.shotaroi.pensionservice.contribution.service;

import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.contribution.mapper.ContributionMapper;
import com.shotaroi.pensionservice.contribution.repository.ContributionRepository;
import com.shotaroi.pensionservice.domain.Contribution;
import com.shotaroi.pensionservice.exception.DuplicateResourceException;
import com.shotaroi.pensionservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private static final BigDecimal CONTRIBUTION_RATE = new BigDecimal("0.185");

    private final ContributionRepository contributionRepository;
    private final CitizenRepository citizenRepository;
    private final ContributionMapper contributionMapper;

    @Transactional
    public ContributionResponse create(ContributionRequest request) {
        if (!citizenRepository.existsById(request.getCitizenId())) {
            throw new ResourceNotFoundException("Citizen", request.getCitizenId());
        }
        if (contributionRepository.existsByCitizenIdAndYear(request.getCitizenId(), request.getYear())) {
            throw new DuplicateResourceException(
                    "Contribution for citizen " + request.getCitizenId() + " and year " + request.getYear() + " already exists");
        }

        Contribution contribution = contributionMapper.toEntity(request);
        contribution.setContributionAmount(calculateContributionAmount(request.getSalary()));
        contribution.setCreatedAt(LocalDateTime.now());
        contribution = contributionRepository.save(contribution);
        return contributionMapper.toResponse(contribution);
    }

    @Transactional(readOnly = true)
    public List<ContributionResponse> getByCitizenId(Long citizenId) {
        if (!citizenRepository.existsById(citizenId)) {
            throw new ResourceNotFoundException("Citizen", citizenId);
        }
        return contributionRepository.findByCitizenIdOrderByYearAsc(citizenId).stream()
                .map(contributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalContributionsByCitizenId(Long citizenId) {
        return contributionRepository.findByCitizenIdOrderByYearAsc(citizenId).stream()
                .map(Contribution::getContributionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateContributionAmount(BigDecimal salary) {
        return salary.multiply(CONTRIBUTION_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
