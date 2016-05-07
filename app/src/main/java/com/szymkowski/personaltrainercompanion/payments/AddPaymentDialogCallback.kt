package com.szymkowski.personaltrainercompanion.payments

import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO

interface AddPaymentDialogCallback {

    fun addPayment(newPayment: PaymentDTO)

}
