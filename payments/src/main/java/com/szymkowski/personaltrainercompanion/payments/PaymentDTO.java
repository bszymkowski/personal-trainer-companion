package com.szymkowski.personaltrainercompanion.payments;

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

    private DateTime paymentDate;
    private int numberOfClassesPaid;
}
