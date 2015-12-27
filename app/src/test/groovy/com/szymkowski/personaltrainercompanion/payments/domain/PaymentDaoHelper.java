package com.szymkowski.personaltrainercompanion.payments.domain;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.core.Database;

import org.joda.time.DateTime;
import org.robolectric.RuntimeEnvironment;

import java.sql.SQLException;

@SuppressWarnings("WeakerAccess")
public class PaymentDaoHelper {

    public static Dao<Payment, Long> getPaymentsDao() throws SQLException {
        return OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Payment.class);
    }

    public static void addPayment() throws SQLException {
        Dao<Payment, Long> dao = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Payment.class);
        dao.create(new Payment(new DateTime(), 8));
        OpenHelperManager.releaseHelper();
    }

}
