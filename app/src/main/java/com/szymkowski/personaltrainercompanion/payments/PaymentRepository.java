package com.szymkowski.personaltrainercompanion.payments;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentMapper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class PaymentRepository {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private Dao<Payment, Long> paymentLongDao;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public PaymentRepository(Database database) {
        try {
            paymentLongDao = database.getDao();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing Payments database!");
        }
    }

    public void addPayment(PaymentDTO paymentDTO) {
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        try {
            paymentLongDao.create(payment);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in adding payment data. Exception: " + e.getMessage());
        }
    }

    public PaymentDTO getLastPayment() {
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
            return null;
        }
        Payment lastPayment= payments.iterator().next();
        result = paymentMapper.paymentToPaymentDTO(lastPayment);
        return result;
    }
}
