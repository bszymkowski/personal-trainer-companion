package com.szymkowski.personaltrainercompanion.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.R;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T, ID> {

    private static final String TAG = BaseRepository.class.getName();
    private final Class klazz;
    protected final Context context;
    protected Dao<T, ID> dao;

    public BaseRepository(Context context){
        this.klazz  = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.context = context;
    }

    protected void create(T entity) {
        Dao<T, ID> dao = getDao();
        try {
            dao.create(entity);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when saving " + klazz.getSimpleName() + "!");
            Toast.makeText(context, context.getResources().getString(R.string.error_saving_entity), Toast.LENGTH_SHORT).show();
        }
        close();
    }

    protected List<T> retrieveAll() {
        Dao<T, ID> dao = getDao();
        List<T> result = new ArrayList<>();
        try {
            result = dao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when retrieving all " + klazz.getSimpleName() + "s!");
            Toast.makeText(context, context.getResources().getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show();
        }
        close();
        return result;
    }

    protected Dao<T, ID> getDao() {
        try {
            dao = OpenHelperManager.getHelper(context, Database.class).getDao(klazz);
            return dao;
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing " + klazz.getSimpleName() + " database!");
            return null;
        }
    }

    protected void close() {
        OpenHelperManager.releaseHelper();
    }




}
