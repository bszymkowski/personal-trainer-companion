package com.szymkowski.personaltrainercompanion.core;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public interface DbCollaborator {

    void closeDomainDb(AndroidConnectionSource connectionSource);

    void createTableIfNotExists(ConnectionSource connectionSource) throws SQLException;
    void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException;


}
