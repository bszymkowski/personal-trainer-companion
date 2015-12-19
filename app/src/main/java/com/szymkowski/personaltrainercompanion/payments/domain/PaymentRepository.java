package com.szymkowski.personaltrainercompanion.payments.domain;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.szymkowski.personaltrainercompanion.core.BaseRepository;
import com.szymkowski.personaltrainercompanion.payments.RepositoryCallback;
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider;

import org.joda.time.DateTimeComparator;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class PaymentRepository extends BaseRepository<Payment, Long> implements PaidNumberOfTrainingsProvider {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;
    private final RepositoryCallback repositoryCallback;

    public PaymentRepository(Context context, RepositoryCallback repositoryCallback) {
        super(context);
        this.repositoryCallback = repositoryCallback;
    }

    public void addPayment(PaymentDTO paymentDTO) {
        Dao<Payment, Long> paymentLongDao = getDao();
        if (paymentLongDao==null) {
            return;
        }
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        Payment previous = getLastPayment(paymentLongDao);
        if (isPaymentOnSameDay(payment, previous)) {
            repositoryCallback.onPaymentAlreadyAdded(paymentDTO);
            close();
            return;
        }
        try {
            paymentLongDao.create(payment);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in adding payment data. Exception: " + e.getMessage());
        } finally {
            close();
        }
    }

    private boolean isPaymentOnSameDay(Payment current, Payment previous) {
        return previous != null && DateTimeComparator.getDateOnlyInstance().compare(current.getPaymentDate(), previous.getPaymentDate()) == 0;
    }

    public PaymentDTO getLastPaymentDTO() {
        Dao<Payment, Long> paymentLongDao = getDao();
        Payment lastPayment = getLastPayment(paymentLongDao);
        PaymentDTO result = paymentMapper.paymentToPaymentDTO(lastPayment);
        close();
        return result;
    }

    private Payment getLastPayment(Dao<Payment, Long> paymentLongDao) {
        if (paymentLongDao==null) {
            return null;
        }
        //noinspection unchecked
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
        return payments.iterator().next();
    }

    public void addPaymentWhenSameDateExists(PaymentDTO paymentDTO) {
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
        close();
    }

    @Override
    public int getNumberOfTrainingsPaidFor() {
        Dao<Payment, Long> paymentLongDao = getDao();
        int result = 0;
        try {
            List<Payment> allPayments = paymentLongDao.queryForAll();
            for (Payment payment : allPayments) {
                result += payment.getNumberOfClassesPaid();
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in counting total number of classes paid. Exception: " + e.getMessage());
        }
        close();
        return result;
    }
}
