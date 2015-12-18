package com.szymkowski.personaltrainercompanion.payments.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


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
