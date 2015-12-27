package com.szymkowski.personaltrainercompanion.payments.domain;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.szymkowski.personaltrainercompanion.core.DbCollaborator;

import java.sql.SQLException;


public class PaymentDomainHelper extends DbCollaborator<Payment> {


    @Override
    public void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        //empty, first table version
    }
}

