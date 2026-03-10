package com.shotaroi.pensionservice.payment.mapper;

import com.shotaroi.pensionservice.domain.PensionPayment;
import com.shotaroi.pensionservice.payment.dto.PensionPaymentResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T00:44:50+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class PensionPaymentMapperImpl implements PensionPaymentMapper {

    @Override
    public PensionPaymentResponse toResponse(PensionPayment payment) {
        if ( payment == null ) {
            return null;
        }

        PensionPaymentResponse.PensionPaymentResponseBuilder pensionPaymentResponse = PensionPaymentResponse.builder();

        pensionPaymentResponse.id( payment.getId() );
        pensionPaymentResponse.citizenId( payment.getCitizenId() );
        pensionPaymentResponse.amount( payment.getAmount() );
        pensionPaymentResponse.paymentDate( payment.getPaymentDate() );
        pensionPaymentResponse.status( payment.getStatus() );
        pensionPaymentResponse.createdAt( payment.getCreatedAt() );

        return pensionPaymentResponse.build();
    }
}
