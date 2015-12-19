package com.szymkowski.personaltrainercompanion.core;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;

public abstract class BaseRepository<T, ID> {

    private static final String TAG = BaseRepository.class.getName();
    private final Class klazz;
    private final Context context;

    public BaseRepository(Context context){
        this.klazz  = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.context = context;
    }

    protected Dao<T, ID> getDao() {
        try {
            return OpenHelperManager.getHelper(context, Database.class).getDao(klazz);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing " + klazz.getSimpleName() + " database!");
            return null;
        }
    }

    protected void close() {
        OpenHelperManager.releaseHelper();
    }


}
