package com.szymkowski.personaltrainercompanion.core;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;

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


    public void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        throw new RuntimeException("not implemented!");
    }

}
