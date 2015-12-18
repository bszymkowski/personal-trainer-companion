package com.szymkowski.personaltrainercompanion.payments.domain;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.szymkowski.personaltrainercompanion.core.DbCollaborator;
import com.szymkowski.personaltrainercompanion.core.DbCore;

import java.sql.SQLException;


    public class PaymentDomainHelper implements DbCollaborator {

        private final DbCore dbCore;
        private Dao<Payment, Long> simpleDao = null;
        private RuntimeExceptionDao<Payment, Long> simpleRuntimeDao = null;

        public PaymentDomainHelper(DbCore dbCore) {
            this.dbCore = dbCore;
        }

        /**
         * Returns the PaymentDomainHelper Access Object (DAO) for our SimpleData class. It will create it or just give the cached
         * value.
         */
        public Dao<Payment, Long> getDao() throws SQLException {
            if (simpleDao == null) {
                simpleDao = dbCore.getDomainDao(Payment.class);
            }
            return simpleDao;
        }

        /**
         * Returns the RuntimeExceptionDao (PaymentDomainHelper Access Object) version of a Dao for our SimpleData class. It will
         * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
         */
        public RuntimeExceptionDao<Payment, Long> getPaymentsDao() {
            if (simpleRuntimeDao == null) {
                simpleRuntimeDao = dbCore.getDomainRuntimeExceptionDao(Payment.class);
            }
            return simpleRuntimeDao;
        }

        /**
         * Close the database connections and clear any cached DAOs.
         * @param connectionSource
         */
        @Override
        public void closeDomainDb(AndroidConnectionSource connectionSource) {
            simpleDao = null;
            simpleRuntimeDao = null;
        }



        @Override
        public void createTableIfNotExists(ConnectionSource connectionSource) throws SQLException {
            Log.i(PaymentDomainHelper.class.getName(), "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, Payment.class);
        }

        @Override
        public void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
            throw new RuntimeException("not implemented!");
        }
    }

