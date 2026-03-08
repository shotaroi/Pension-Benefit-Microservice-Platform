package com.shotaroi.pensionservice.payment.listener;

import com.shotaroi.pensionservice.config.JmsConfig;
import com.shotaroi.pensionservice.messaging.PensionCalculatedEvent;
import com.shotaroi.pensionservice.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@org.springframework.context.annotation.Profile("!test")
public class PensionCalculatedEventListener {

    private final PaymentService paymentService;

    @JmsListener(destination = JmsConfig.PENSION_CALCULATED_QUEUE)
    public void onPensionCalculated(PensionCalculatedEvent event) {
        log.info("Received PensionCalculatedEvent for citizen {}: estimated monthly pension={}",
                event.getCitizenId(), event.getEstimatedMonthlyPension());

        paymentService.generateFirstPayment(event.getCitizenId(), event.getEstimatedMonthlyPension());
    }
}
