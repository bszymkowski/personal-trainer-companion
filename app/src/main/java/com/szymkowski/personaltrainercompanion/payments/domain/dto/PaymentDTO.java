package com.szymkowski.personaltrainercompanion.payments.domain.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class PaymentDTO {

    private Date paymentDate;
    private int numberOfClassesPaid;
}
