package com.szymkowski.personaltrainercompanion.core;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;

interface DbCore {
    Dao getDomainDao(Class<?> klazz) throws SQLException;
    RuntimeExceptionDao getDomainRuntimeExceptionDao(Class<?> klazz);
}
