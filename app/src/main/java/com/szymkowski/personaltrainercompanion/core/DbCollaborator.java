package com.szymkowski.personaltrainercompanion.core;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;

/*
This whole byzantine construct only serves to prevent domain entity objects from flying around the whole application.
I want them only available from their packages.
 */

public abstract class DbCollaborator<T> {

    private final Class klazz;

    public DbCollaborator() {
        this.klazz  = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void createTableIfNotExists(ConnectionSource connectionSource) throws SQLException {
        Log.i(DbCollaborator.this.getClass().getName(), "onCreate");
        TableUtils.createTableIfNotExists(connectionSource, klazz);
    }


    public abstract void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException;

}
