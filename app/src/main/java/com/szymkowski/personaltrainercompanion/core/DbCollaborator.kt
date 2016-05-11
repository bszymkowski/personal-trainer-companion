package com.szymkowski.personaltrainercompanion.core

import android.database.sqlite.SQLiteDatabase
import android.util.Log

import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import java.lang.reflect.ParameterizedType
import java.sql.SQLException

/*
This whole byzantine construct only serves to prevent domain entity objects from flying around the whole application.
I want them only available from their packages.
 */

abstract class DbCollaborator<T> protected constructor() {

    private val klazz: Class<Any>

    init {
        this.klazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<Any>
    }

    @Throws(SQLException::class)
    fun createTableIfNotExists(connectionSource: ConnectionSource) {
        Log.i(this@DbCollaborator.javaClass.name, "onCreate")
        TableUtils.createTableIfNotExists(connectionSource, klazz)
    }


    @SuppressWarnings("EmptyMethod", "RedundantThrows", "UnusedParameters")
    @Throws(SQLException::class)
    abstract fun upgradeTable(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int)

}
