package com.shotaroi.pensionservice.citizen.controller;

import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.contribution.service.ContributionService;
import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.citizen.service.CitizenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/citizens")
@RequiredArgsConstructor
@Tag(name = "Citizen Service", description = "Register and manage citizens for pension eligibility")
public class CitizenController {

    private final CitizenService citizenService;
    private final ContributionService contributionService;

    @PostMapping
    @Operation(summary = "Register a new citizen")
    public ResponseEntity<CitizenResponse> create(@Valid @RequestBody CitizenRequest request) {
        CitizenResponse response = citizenService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get citizen by ID")
    public ResponseEntity<CitizenResponse> getById(@PathVariable Long id) {
        CitizenResponse response = citizenService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all citizens with pagination")
    public ResponseEntity<Page<CitizenResponse>> getAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<CitizenResponse> response = citizenService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/contributions")
    @Operation(summary = "Get all contributions for a citizen")
    public ResponseEntity<List<ContributionResponse>> getContributions(@PathVariable("id") Long citizenId) {
        List<ContributionResponse> response = contributionService.getByCitizenId(citizenId);
        return ResponseEntity.ok(response);
    }
}
