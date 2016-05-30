package com.szymkowski.personaltrainercompanion.core

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.support.ConnectionSource
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDomainHelper
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingDomainHelper
import java.sql.SQLException
import java.util.*

class Database(context: Context) : OrmLiteSqliteOpenHelper(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION), DbCore {

    //yes, it is a bad practice to put private methods so high up. It's justified here.
    private fun createCollaboratorsList() {
        DB_COLLABORATORS.add(TrainingDomainHelper())
        DB_COLLABORATORS.add(PaymentDomainHelper())
    }

    init {
        createCollaboratorsList()
    }//// FIXME: 06.12.2015 add config file

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            Log.i(Database::class.java.name, "onCreate")
            for (collaborator in DB_COLLABORATORS) {
                collaborator.createTableIfNotExists(connectionSource)
            }
        } catch (e: SQLException) {
            Log.e(Database::class.java.name, "Can't create database", e)
            throw RuntimeException(e)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        try {
            Log.i(PaymentDomainHelper::class.java.name, "onUpgrade")
            for (collaborator in DB_COLLABORATORS) {
                collaborator.upgradeTable(db, connectionSource, oldVersion, newVersion)
            }
        } catch (e: SQLException) {
            Log.e(Database::class.java.name, "Can't update databases", e)
            throw RuntimeException(e)
        }

    }

    @Throws(SQLException::class)
    //todo take a look at them "Any"
    override fun getDomainDao(klazz: Class<*>): Dao<Any, Any> {
        return getDao<Dao<Any, Any>, Any>(klazz as Class<Any>)
    }

    override fun getDomainRuntimeExceptionDao(klazz: Class<*>): RuntimeExceptionDao<Any, Any> {
        //todo take a look at them "Any"
        return getRuntimeExceptionDao<RuntimeExceptionDao<Any, Any>, Any>(klazz as Class<Any>)
    }

    companion object {

        //todo change this to <Any>
        private val DB_COLLABORATORS = HashSet<DbCollaborator<*>>()

        private val DATABASE_NAME = "personal_trainer_companion.db"
        private val DATABASE_VERSION = 1
    }
}
