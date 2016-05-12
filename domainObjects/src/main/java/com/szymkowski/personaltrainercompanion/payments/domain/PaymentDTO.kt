package com.szymkowski.personaltrainercompanion.payments.domain

import org.joda.time.DateTime


data class PaymentDTO (val id : Long) {

    constructor(paymentDate: DateTime, numberOfClassesPaid: Int) : this(0L){
        this.paymentDate = paymentDate
        this.numberOfClassesPaid = numberOfClassesPaid
    }

    constructor() : this(0L)

    lateinit var paymentDate: DateTime

    var numberOfClassesPaid: Int = 0

}
