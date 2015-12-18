package com.szymkowski.personaltrainercompanion.trainings.domain;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.szymkowski.personaltrainercompanion.core.Database;
import com.szymkowski.personaltrainercompanion.core.DbCollaborator;
import com.szymkowski.personaltrainercompanion.core.DbCore;

import java.sql.SQLException;

public class TrainingDomainHelper implements DbCollaborator {

    private final DbCore dbCore;
    private Dao<Training, Long> simpleDao = null;
    private RuntimeExceptionDao<Training, Long> simpleRuntimeDao = null;

    public TrainingDomainHelper(Database database) {
        dbCore = database;
    }

    @Override
    public void closeDomainDb(ConnectionSource connectionSource) {
        simpleDao = null;
        simpleRuntimeDao = null;
    }

    @Override
    public void createTableIfNotExists(ConnectionSource connectionSource) throws SQLException {
        Log.i(TrainingDomainHelper.class.getName(), "onCreate");
        TableUtils.createTableIfNotExists(connectionSource, Training.class);
    }

    @Override
    public void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        throw new RuntimeException("not implemented!");
    }
}
