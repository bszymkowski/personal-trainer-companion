package com.szymkowski.personaltrainercompanion.payments;

import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO;

public interface RepositoryCallback {
    void onPaymentAlreadyAdded(PaymentDTO paymentDTO);
}
