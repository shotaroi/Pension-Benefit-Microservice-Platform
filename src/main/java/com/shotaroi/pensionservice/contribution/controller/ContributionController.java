package com.shotaroi.pensionservice.contribution.controller;

import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.contribution.service.ContributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/contributions")
@RequiredArgsConstructor
@Tag(name = "Contribution Service", description = "Manage pension contributions per year")
public class ContributionController {

    private final ContributionService contributionService;

    @PostMapping
    @Operation(summary = "Create a new contribution record")
    public ResponseEntity<ContributionResponse> create(@Valid @RequestBody ContributionRequest request) {
        ContributionResponse response = contributionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/citizens/{citizenId}")
    @Operation(summary = "Get all contributions for a citizen")
    public ResponseEntity<List<ContributionResponse>> getByCitizenId(@PathVariable Long citizenId) {
        List<ContributionResponse> response = contributionService.getByCitizenId(citizenId);
        return ResponseEntity.ok(response);
    }
}
