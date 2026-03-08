package com.shotaroi.pensionservice.pension.controller;

import com.shotaroi.pensionservice.pension.dto.PensionCalculationRequest;
import com.shotaroi.pensionservice.pension.dto.PensionCalculationResponse;
import com.shotaroi.pensionservice.pension.service.PensionCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pension")
@RequiredArgsConstructor
@Tag(name = "Pension Calculation Service", description = "Calculate estimated pension based on contributions")
public class PensionController {

    private final PensionCalculationService pensionCalculationService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate estimated monthly pension")
    public ResponseEntity<PensionCalculationResponse> calculate(@Valid @RequestBody PensionCalculationRequest request) {
        PensionCalculationResponse response = pensionCalculationService.calculate(request);
        return ResponseEntity.ok(response);
    }
}
