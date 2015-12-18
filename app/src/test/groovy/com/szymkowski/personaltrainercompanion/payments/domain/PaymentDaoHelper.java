package com.szymkowski.personaltrainercompanion.payments.domain;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.core.Database;

import org.robolectric.RuntimeEnvironment;

import java.sql.SQLException;

public class PaymentDaoHelper {

    public static Dao getPaymentsDao() throws SQLException {
        return OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Payment.class);
    }

}
