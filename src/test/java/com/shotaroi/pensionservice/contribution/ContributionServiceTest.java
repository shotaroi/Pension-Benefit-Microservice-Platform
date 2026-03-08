package com.shotaroi.pensionservice.contribution;

import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.contribution.mapper.ContributionMapper;
import com.shotaroi.pensionservice.contribution.repository.ContributionRepository;
import com.shotaroi.pensionservice.contribution.service.ContributionService;
import com.shotaroi.pensionservice.domain.Contribution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContributionServiceTest {

    @Mock
    private ContributionRepository contributionRepository;

    @Mock
    private CitizenRepository citizenRepository;

    @Mock
    private ContributionMapper contributionMapper;

    @InjectMocks
    private ContributionService contributionService;

    @Test
    void create_shouldCalculateContributionAmountCorrectly() {
        ContributionRequest request = ContributionRequest.builder()
                .citizenId(1L)
                .year(2023)
                .salary(new BigDecimal("500000"))
                .build();

        Contribution entity = Contribution.builder()
                .citizenId(1L)
                .year(2023)
                .salary(new BigDecimal("500000"))
                .build();

        Contribution savedEntity = Contribution.builder()
                .id(1L)
                .citizenId(1L)
                .year(2023)
                .salary(new BigDecimal("500000"))
                .contributionAmount(new BigDecimal("92500.00"))  // 500000 * 0.185
                .createdAt(LocalDateTime.now())
                .build();

        ContributionResponse response = ContributionResponse.builder()
                .id(1L)
                .citizenId(1L)
                .year(2023)
                .salary(new BigDecimal("500000"))
                .contributionAmount(new BigDecimal("92500.00"))
                .build();

        when(citizenRepository.existsById(1L)).thenReturn(true);
        when(contributionRepository.existsByCitizenIdAndYear(1L, 2023)).thenReturn(false);
        when(contributionMapper.toEntity(request)).thenReturn(entity);
        when(contributionRepository.save(any(Contribution.class))).thenReturn(savedEntity);
        when(contributionMapper.toResponse(savedEntity)).thenReturn(response);

        ContributionResponse result = contributionService.create(request);

        assertThat(result.getContributionAmount()).isEqualByComparingTo("92500.00");
        assertThat(result.getSalary()).isEqualByComparingTo("500000");
        verify(contributionRepository).save(any(Contribution.class));
    }
}
