package com.shotaroi.pensionservice.citizen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitizenRequest {

    @NotBlank(message = "Personal number is required")
    @Pattern(regexp = "\\d{8}-\\d{4}", message = "Personal number must match format YYYYMMDD-XXXX")
    private String personalNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
}
