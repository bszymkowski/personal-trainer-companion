package com.szymkowski.personaltrainercompanion.payments.domain

import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.support.ConnectionSource
import com.szymkowski.personaltrainercompanion.core.DbCollaborator

import java.sql.SQLException


class PaymentDomainHelper : DbCollaborator<Payment>() {


    @Throws(SQLException::class)
    override fun upgradeTable(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        //empty, first table version
    }
}

