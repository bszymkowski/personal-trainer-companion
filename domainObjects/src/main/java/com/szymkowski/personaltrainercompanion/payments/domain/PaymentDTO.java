package com.szymkowski.personaltrainercompanion.payments.domain;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private DateTime paymentDate;
    private int numberOfClassesPaid;

    public PaymentDTO(DateTime paymentDate, int numberOfClassesPaid) {
        this.id = 0L;
        this.paymentDate = paymentDate;
        this.numberOfClassesPaid = numberOfClassesPaid;
    }
}
