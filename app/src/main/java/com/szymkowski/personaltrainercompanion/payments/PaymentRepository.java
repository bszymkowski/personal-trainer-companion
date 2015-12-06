package com.szymkowski.personaltrainercompanion.payments;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.szymkowski.personaltrainercompanion.payments.domain.db.Payment;
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentDTO;
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentMapper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class PaymentRepository {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private Dao<Payment, Long> paymentLongDao;
    private PaymentMapper mapper = PaymentMapper.INSTANCE;

    public PaymentRepository(ConnectionSource connectionSource) {
        try {
            paymentLongDao = DaoManager.createDao(connectionSource, Payment.class);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in creating PaymentRepository. Exception: " + e.getMessage());
        }

    }

    public void addPayment(PaymentDTO paymentDTO) {
        Payment payment = mapper.paymentDtoToPayment(paymentDTO);
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
        Payment lastPayment= payments.iterator().next();
        result = mapper.paymentToPaymentDTO(lastPayment);
        return result;
    }
}
