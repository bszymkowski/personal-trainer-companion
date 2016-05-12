package com.szymkowski.personaltrainercompanion.payments.domain

import org.joda.time.DateTime

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
class PaymentDTO {
    var id: Long?
        set(id) {
            this.id = id
        }
    var paymentDate: DateTime
        set(paymentDate) {
            this.paymentDate = paymentDate
        }
    var numberOfClassesPaid: Int
        set(numberOfClassesPaid) {
            this.numberOfClassesPaid = numberOfClassesPaid
        }

    constructor(paymentDate: DateTime, numberOfClassesPaid: Int) {
        this.id = 0L
        this.paymentDate = paymentDate
        this.numberOfClassesPaid = numberOfClassesPaid
    }
}
