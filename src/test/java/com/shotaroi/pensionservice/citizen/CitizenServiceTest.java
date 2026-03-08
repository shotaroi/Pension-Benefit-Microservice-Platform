package com.shotaroi.pensionservice.citizen;

import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.citizen.mapper.CitizenMapper;
import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.citizen.service.CitizenService;
import com.shotaroi.pensionservice.domain.Citizen;
import com.shotaroi.pensionservice.exception.DuplicateResourceException;
import com.shotaroi.pensionservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CitizenServiceTest {

    @Mock
    private CitizenRepository citizenRepository;

    @Mock
    private CitizenMapper citizenMapper;

    @InjectMocks
    private CitizenService citizenService;

    @Test
    void create_shouldSaveAndReturnCitizen() {
        CitizenRequest request = CitizenRequest.builder()
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .build();

        Citizen entity = Citizen.builder()
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .build();

        Citizen savedEntity = Citizen.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        CitizenResponse response = CitizenResponse.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        when(citizenRepository.existsByPersonalNumber("19700101-1234")).thenReturn(false);
        when(citizenMapper.toEntity(request)).thenReturn(entity);
        when(citizenRepository.save(any(Citizen.class))).thenReturn(savedEntity);
        when(citizenMapper.toResponse(savedEntity)).thenReturn(response);

        CitizenResponse result = citizenService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Anna");
        verify(citizenRepository).save(any(Citizen.class));
    }

    @Test
    void create_shouldThrowWhenPersonalNumberExists() {
        CitizenRequest request = CitizenRequest.builder()
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .build();

        when(citizenRepository.existsByPersonalNumber("19700101-1234")).thenReturn(true);

        assertThatThrownBy(() -> citizenService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("19700101-1234");

        verify(citizenRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnCitizen() {
        Citizen citizen = Citizen.builder()
                .id(1L)
                .personalNumber("19700101-1234")
                .firstName("Anna")
                .lastName("Svensson")
                .birthDate(LocalDate.of(1970, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        CitizenResponse response = CitizenResponse.builder()
                .id(1L)
                .firstName("Anna")
                .build();

        when(citizenRepository.findById(1L)).thenReturn(Optional.of(citizen));
        when(citizenMapper.toResponse(citizen)).thenReturn(response);

        CitizenResponse result = citizenService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Anna");
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(citizenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citizenService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Citizen")
                .hasMessageContaining("999");
    }
}
