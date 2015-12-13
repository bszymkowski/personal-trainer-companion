package com.szymkowski.personaltrainercompanion.payments.addpayment;

import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO;

public interface RepositoryCallback {
    void onPaymentAlreadyAdded(PaymentDTO paymentDTO);
}
