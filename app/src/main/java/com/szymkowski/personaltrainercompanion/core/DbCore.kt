package com.szymkowski.personaltrainercompanion.core


import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao

import java.sql.SQLException

internal interface DbCore {
    @Throws(SQLException::class)
    fun getDomainDao(klazz: Class<*>): Dao<Any, Any>

    fun getDomainRuntimeExceptionDao(klazz: Class<*>): RuntimeExceptionDao<Any, Any>
}
