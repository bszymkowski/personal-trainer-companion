package com.szymkowski.personaltrainercompanion.payments.domain;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class PaymentRepository {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private final Context context;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public PaymentRepository(Context context) {
        this.context = context;
    }

    public void addPayment(PaymentDTO paymentDTO) {
        Dao<Payment, Long> paymentLongDao = getDao();
        if (paymentLongDao==null) {
            return;
        }
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        try {
            paymentLongDao.create(payment);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in adding payment data. Exception: " + e.getMessage());
        }
        OpenHelperManager.releaseHelper();
    }

    public PaymentDTO getLastPayment() {
        Dao<Payment, Long> paymentLongDao = getDao();
        if (paymentLongDao==null) {
            return null;
        }
        PaymentDTO result;
        List<Payment> payments = Collections.EMPTY_LIST;
        QueryBuilder<Payment, Long> builder = paymentLongDao.queryBuilder();
        builder.orderBy(Payment.PAYMENT_DATE_COLUMN, false).limit(1L);
        try {
            PreparedQuery<Payment> paymentPreparedQuery  = builder.prepare();
            payments = paymentLongDao.query(paymentPreparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception while retrieving last payment data. Exception: " + e.getMessage());
        }
        if (payments.isEmpty()) {
            OpenHelperManager.releaseHelper();
            return null;
        }
        Payment lastPayment= payments.iterator().next();
        result = paymentMapper.paymentToPaymentDTO(lastPayment);
        OpenHelperManager.releaseHelper();
        return result;
    }

    private Dao<Payment, Long> getDao() {
        try {
            return OpenHelperManager.getHelper(context, Database.class).getDao();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing Payments database!");
            return null;
        }
    }
}
