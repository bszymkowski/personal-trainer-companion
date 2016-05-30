package com.szymkowski.personaltrainercompanion.payments.domain

import org.joda.time.DateTime


data class PaymentDTO (
        val id : Long,
        var paymentDate: DateTime,
        var numberOfClassesPaid: Int
) {

    constructor(paymentDate: DateTime, numberOfClassesPaid: Int) : this(0L, paymentDate, numberOfClassesPaid)

    constructor() : this(0L, DateTime.now(), 0)

}
