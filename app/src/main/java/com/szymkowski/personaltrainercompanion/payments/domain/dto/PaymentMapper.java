package com.szymkowski.personaltrainercompanion.payments.domain.dto;

import com.szymkowski.personaltrainercompanion.payments.Payment;
import com.szymkowski.personaltrainercompanion.payments.PaymentDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


/*
This is probably the biggest overkill ever, to use mapstruct just for this one mapper.
The heck, I wanted to learn how to do this.
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mappings({
            @Mapping(source = "paymentDate", target = "paymentDate"),
            @Mapping(source = "numberOfClassesPaid", target = "numberOfClassesPaid")
    })
    PaymentDTO paymentToPaymentDTO(Payment payment);

    @Mappings({
            @Mapping(source = "paymentDate", target = "paymentDate"),
            @Mapping(source = "numberOfClassesPaid", target = "numberOfClassesPaid")
    })
    Payment paymentDTOToPayment(PaymentDTO paymentDTO);

}
