package com.shotaroi.pensionservice.payment.mapper;

import com.shotaroi.pensionservice.domain.PensionPayment;
import com.shotaroi.pensionservice.payment.dto.PensionPaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PensionPaymentMapper {

    PensionPaymentResponse toResponse(PensionPayment payment);
}
