package com.szymkowski.personaltrainercompanion.trainings.domain;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.szymkowski.personaltrainercompanion.core.DbCollaborator;

import java.sql.SQLException;

public class TrainingDomainHelper extends DbCollaborator<Training> {

    @Override
    public void upgradeTable(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        //empty, first table version
    }
}
