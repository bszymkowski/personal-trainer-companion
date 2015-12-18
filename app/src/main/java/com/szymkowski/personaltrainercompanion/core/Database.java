package com.szymkowski.personaltrainercompanion.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDomainHelper;
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingDomainHelper;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Database extends OrmLiteSqliteOpenHelper implements DbCore{

    private static final Set<DbCollaborator> DB_COLLABORATORS = new HashSet<>();

    //yes, it is a bad practice to put private methods so high up. It's justified here.
    private void createCollaboratorsList() {
        DB_COLLABORATORS.add(new TrainingDomainHelper(this));
        DB_COLLABORATORS.add(new PaymentDomainHelper(this));
    }

    private static final String DATABASE_NAME = "personal_trainer_companion.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        //// FIXME: 06.12.2015 add config file
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        createCollaboratorsList();  
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(Database.class.getName(), "onCreate");
            for (DbCollaborator collaborator : DB_COLLABORATORS) {
                collaborator.createTableIfNotExists(connectionSource);
            }
        } catch (SQLException e) {
            Log.e(Database.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(PaymentDomainHelper.class.getName(), "onUpgrade");
            for (DbCollaborator collaborator : DB_COLLABORATORS) {
                collaborator.upgradeTable(db, connectionSource, oldVersion, newVersion);
            }
        } catch (SQLException e) {
            Log.e(Database.class.getName(), "Can't update databases", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dao getDomainDao(Class<?> klazz)throws SQLException {
        return getDao(klazz);
    }

    @Override
    public void close() {
        super.close();
        for (DbCollaborator collaborator : DB_COLLABORATORS) {
            collaborator.closeDomainDb(connectionSource);
        }
    }

    @Override
    public RuntimeExceptionDao getDomainRuntimeExceptionDao(Class<?> klazz) {
        return getRuntimeExceptionDao(klazz);
    }
}
