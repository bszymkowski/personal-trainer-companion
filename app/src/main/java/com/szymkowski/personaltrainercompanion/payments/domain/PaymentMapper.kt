package com.szymkowski.personaltrainercompanion.payments.domain



object PaymentMapper {

    fun paymentToPaymentDTO(payment: Payment): PaymentDTO {
        val (id, date, number) = payment
        return PaymentDTO(id, date, number)
    }

    fun paymentDTOToPayment(paymentDTO: PaymentDTO): Payment {
        val (id, date, number) = paymentDTO
        return Payment(id, date, number)
    }

}
