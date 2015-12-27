package com.szymkowski.personaltrainercompanion.payments.domain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.R;
import com.szymkowski.personaltrainercompanion.core.BaseRepository;
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback;
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider;

import org.joda.time.DateTimeComparator;

import java.sql.SQLException;
import java.util.List;


public class PaymentRepository extends BaseRepository<Payment, Long> implements PaidNumberOfTrainingsProvider {

    private static final String TAG = PaymentRepository.class.getSimpleName();
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;
    private final RepositoryCallback callback;

    public PaymentRepository(Context context, RepositoryCallback callback) {
        super(context);
        this.callback = callback;
    }

    public void addPayment(final PaymentDTO paymentDTO) {
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        Payment previous = getLatest();
        if (isPaymentOnSameDay(payment, previous)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.payment_already_added_today_title);
            builder.setMessage(R.string.payment_already_added_today_message);
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    create(paymentMapper.paymentDTOToPayment(paymentDTO));
                    callback.onDatasetChanged();
                    dialog.dismiss();
                }
            });
            builder.show();
            return;
        } else {
            create(paymentMapper.paymentDTOToPayment(paymentDTO));
            callback.onDatasetChanged();
        }
    }

    private boolean isPaymentOnSameDay(Payment current, Payment previous) {
        return previous != null && DateTimeComparator.getDateOnlyInstance().compare(current.getDate(), previous.getDate()) == 0;
    }

    public PaymentDTO getLastPaymentDTO() {
        Payment lastPayment = getLatest();
        PaymentDTO result = paymentMapper.paymentToPaymentDTO(lastPayment);
        return result;
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
