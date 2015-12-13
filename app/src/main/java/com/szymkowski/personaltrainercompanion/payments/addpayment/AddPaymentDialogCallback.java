package com.szymkowski.personaltrainercompanion.payments.addpayment;

import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO;

public interface AddPaymentDialogCallback {

    void addPayment(PaymentDTO newPayment);

}
