package com.shotaroi.pensionservice.pension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotaroi.pensionservice.TestContainersConfig;
import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.pension.dto.PensionCalculationRequest;
import com.shotaroi.pensionservice.pension.dto.PensionCalculationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@org.springframework.context.annotation.Import({TestContainersConfig.class, com.shotaroi.pensionservice.StubJmsConfig.class})
class PensionCalculationIntegrationTest {

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestContainersConfig.postgres::getJdbcUrl);
        registry.add("spring.datasource.username", TestContainersConfig.postgres::getUsername);
        registry.add("spring.datasource.password", TestContainersConfig.postgres::getPassword);
    }

    @org.springframework.boot.test.mock.mockito.MockBean
    private org.springframework.jms.core.JmsTemplate jmsTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Full flow: Create citizen -> Add contributions -> Calculate pension")
    void pensionCalculation_fullFlow() throws Exception {
        // 1. Create citizen
        CitizenRequest citizenRequest = CitizenRequest.builder()
                .personalNumber("19700101-5678")
                .firstName("Erik")
                .lastName("Johansson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .build();

        MvcResult citizenResult = mockMvc.perform(post("/citizens")
                        .with(httpBasic("pension_admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citizenRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        CitizenResponse citizen = objectMapper.readValue(
                citizenResult.getResponse().getContentAsString(), CitizenResponse.class);
        Long citizenId = citizen.getId();

        // 2. Add contributions for multiple years
        // Year 2020: 400000 * 0.185 = 74000
        // Year 2021: 450000 * 0.185 = 83250
        // Year 2022: 500000 * 0.185 = 92500
        // Total = 249750, monthly = 249750/240 = 1040.62
        for (int year = 2020; year <= 2022; year++) {
            BigDecimal salary = BigDecimal.valueOf(400000 + (year - 2020) * 50000);
            ContributionRequest contributionRequest = ContributionRequest.builder()
                    .citizenId(citizenId)
                    .year(year)
                    .salary(salary)
                    .build();

            mockMvc.perform(post("/contributions")
                            .with(httpBasic("pension_admin", "admin123"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(contributionRequest)))
                    .andExpect(status().isCreated());
        }

        // 3. Calculate pension
        PensionCalculationRequest calcRequest = PensionCalculationRequest.builder()
                .citizenId(citizenId)
                .retirementAge(65)
                .build();

        MvcResult calcResult = mockMvc.perform(post("/pension/calculate")
                        .with(httpBasic("pension_admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calcRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citizenId").value(citizenId))
                .andExpect(jsonPath("$.estimatedMonthlyPension").exists())
                .andReturn();

        PensionCalculationResponse calcResponse = objectMapper.readValue(
                calcResult.getResponse().getContentAsString(), PensionCalculationResponse.class);

        // 74000 + 83250 + 92500 = 249750, 249750/240 = 1040.625 (rounds to 1040.62 or 1040.63)
        assertThat(calcResponse.getEstimatedMonthlyPension())
                .isBetween(new BigDecimal("1040.60"), new BigDecimal("1040.65"));
    }
}
