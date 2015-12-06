package com.szymkowski.personaltrainercompanion.payments;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.szymkowski.personaltrainercompanion.OrmDbHelper;
import com.szymkowski.personaltrainercompanion.payments.domain.db.Payment;
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class PaymentRepository {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private Dao<Payment, Long> paymentLongDao;
//    private PaymentMapper mapper = PaymentMapper.INSTANCE;

    public PaymentRepository(Context context) {
        try {
            paymentLongDao = OpenHelperManager.getHelper(context, OrmDbHelper.class).getDao();;
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in creating PaymentRepository. Exception: " + e.getMessage());
        }

    }

    public void addPayment(PaymentDTO paymentDTO) {
        Payment payment = getPaymentFromDto(paymentDTO);
        try {
            paymentLongDao.create(payment);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in adding payment data. Exception: " + e.getMessage());
        }
    }

    public PaymentDTO getLastPayment() {
        PaymentDTO result;
        List<Payment> payments = Collections.EMPTY_LIST;
        QueryBuilder builder = paymentLongDao.queryBuilder();
        builder.orderBy(Payment.PAYMENT_DATE_COLUMN, true).limit(1L);
        try {
            PreparedQuery<Payment> paymentPreparedQuery  = builder.prepare();
            payments = paymentLongDao.query(paymentPreparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception while retrieving last payment data. Exception: " + e.getMessage());
        }
        //// FIXME: 07.12.2015
        if (payments.isEmpty()) {
            return null;
        }
        Payment lastPayment= payments.iterator().next();
        result = getDtoFromEntity(lastPayment);
        return result;
    }

    //// FIXME: 06.12.2015
    private PaymentDTO getDtoFromEntity(Payment payment) {
        PaymentDTO dto = new PaymentDTO(payment.getPaymentDate(), payment.getNumberOfClassesPaid());
        return dto;
    }

    //// FIXME: 06.12.2015
    private Payment getPaymentFromDto(PaymentDTO paymentDTO) {
        Payment payment = new Payment(0L, paymentDTO.getPaymentDate(), paymentDTO.getNumberOfClassesPaid());
        return payment;
    }
}
