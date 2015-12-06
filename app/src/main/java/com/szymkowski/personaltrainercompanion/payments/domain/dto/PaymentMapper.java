package com.szymkowski.personaltrainercompanion.payments.domain.dto;

import com.szymkowski.personaltrainercompanion.payments.domain.db.Payment;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDTO paymentToPaymentDTO(Payment payment);

    Payment paymentDtoToPayment(PaymentDTO paymentDTO);
}
