package com.shotaroi.pensionservice.payment.controller;

import com.shotaroi.pensionservice.payment.dto.GeneratePaymentRequest;
import com.shotaroi.pensionservice.payment.dto.PensionPaymentResponse;
import com.shotaroi.pensionservice.payment.service.PaymentService;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Service", description = "Handle monthly pension payouts")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/generate")
    @Operation(summary = "Generate first payment (typically triggered by PensionCalculatedEvent via JMS)")
    public ResponseEntity<PensionPaymentResponse> generate(@Valid @RequestBody GeneratePaymentRequest request) {
        PensionPaymentResponse response = paymentService.generateFirstPayment(
                request.getCitizenId(), request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{citizenId}")
    @Operation(summary = "Get all payments for a citizen")
    public ResponseEntity<List<PensionPaymentResponse>> getByCitizenId(@PathVariable Long citizenId) {
        List<PensionPaymentResponse> response = paymentService.getByCitizenId(citizenId);
        return ResponseEntity.ok(response);
    }
}
