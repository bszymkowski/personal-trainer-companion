package com.szymkowski.personaltrainercompanion.trainings.domain

import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.support.ConnectionSource
import com.szymkowski.personaltrainercompanion.core.DbCollaborator

import java.sql.SQLException

class TrainingDomainHelper : DbCollaborator<Training>() {

    @Throws(SQLException::class)
    override fun upgradeTable(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        //empty, first table version
    }
}
