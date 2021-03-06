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
    private final RepositoryCallback callback;
    private Dao<T, ID> dao;

    protected BaseRepository(Context context, RepositoryCallback callback){
        this.klazz  = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.context = context;
        this.callback = callback;
    }

    protected void create(T entity) {
        Dao<T, ID> dao = getDao();
        try {
            dao.create(entity);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when saving " + klazz.getSimpleName() + "!");
            Toast.makeText(context, context.getResources().getString(R.string.error_saving_entity), Toast.LENGTH_SHORT).show();
        }
        callback.onDatasetChanged();
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
            //noinspection unchecked
            dao = OpenHelperManager.getHelper(context, Database.class).getDao(klazz);
            return dao;
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing " + klazz.getSimpleName() + " database!");
            Toast.makeText(context, context.getResources().getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    protected T getLatest() {
        T entity = null;
        getDao();
        try {
            entity = dao.queryBuilder()
                    .orderBy(BaseEntity.DATE_COLUMN, false)
                    .limit(1L)
                    .queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when accessing " + klazz.getSimpleName() + " database!");
            Toast.makeText(context, context.getResources().getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show();
        } finally {
            close();
        }
        return entity;
    }

    protected void close() {
        OpenHelperManager.releaseHelper();
    }




}
