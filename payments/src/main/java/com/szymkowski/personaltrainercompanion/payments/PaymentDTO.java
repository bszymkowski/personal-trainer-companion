package com.szymkowski.personaltrainercompanion.payments;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentDTO {

    private Date paymentDate;
    private int numberOfClassesPaid;

}
