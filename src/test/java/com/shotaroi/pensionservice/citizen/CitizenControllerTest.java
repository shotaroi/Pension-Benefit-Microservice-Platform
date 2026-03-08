package com.shotaroi.pensionservice.citizen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.service.CitizenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(com.shotaroi.pensionservice.citizen.controller.CitizenController.class)
@Import(com.shotaroi.pensionservice.config.SecurityConfig.class)
@org.springframework.boot.autoconfigure.EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration.class
})
class CitizenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CitizenService citizenService;

    @MockBean
    private com.shotaroi.pensionservice.contribution.service.ContributionService contributionService;

    @MockBean
    private jakarta.jms.ConnectionFactory connectionFactory;

    @Test
    @WithMockUser
    void createCitizen_shouldReturn201() throws Exception {
        CitizenRequest request = CitizenRequest.builder()
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .build();

        var response = com.shotaroi.pensionservice.citizen.dto.CitizenResponse.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        when(citizenService.create(any(CitizenRequest.class))).thenReturn(response);

        mockMvc.perform(post("/citizens")
                        .with(httpBasic("pension_admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Svensson"))
                .andExpect(jsonPath("$.personalNumber").value("19700101-1234"));

        verify(citizenService).create(any(CitizenRequest.class));
    }

    @Test
    @WithMockUser
    void getCitizenById_shouldReturn200() throws Exception {
        var response = com.shotaroi.pensionservice.citizen.dto.CitizenResponse.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        when(citizenService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/citizens/1").with(httpBasic("pension_admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Anna"));

        verify(citizenService).getById(1L);
    }

    @Test
    @WithMockUser
    void getAllCitizens_shouldReturn200() throws Exception {
        var citizen = com.shotaroi.pensionservice.citizen.dto.CitizenResponse.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        when(citizenService.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(citizen)));

        mockMvc.perform(get("/citizens").with(httpBasic("pension_admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Anna"));

        verify(citizenService).getAll(any(Pageable.class));
    }
}
