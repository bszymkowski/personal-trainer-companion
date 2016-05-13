package com.szymkowski.personaltrainercompanion.core

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.szymkowski.personaltrainercompanion.R
import java.lang.reflect.ParameterizedType
import java.sql.SQLException
import java.util.*

abstract class BaseRepository<T, ID> protected constructor(protected val context: Context, private val callback: RepositoryCallback ) {
    private val klazz: Class<T>
    private var dao: Dao<T, ID>? = null

    init {
        this.klazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class <T>
    }

    protected fun create(entity: T) {
        val dao = getDao()
        try {
            dao?.create(entity)
        } catch (e: SQLException) {
            Log.e(TAG, "SQLite exception when saving " + klazz.simpleName + "!")
            Toast.makeText(context, context.resources.getString(R.string.error_saving_entity), Toast.LENGTH_SHORT).show()
        }

        callback.onDatasetChanged()
        close()
    }

    protected fun retrieveAll(): List<T> {
        val dao = getDao()
        var result : List<T> = ArrayList<T>()
        try {
            result = dao?.queryForAll() as List<T>
        } catch (e: SQLException) {
            Log.e(TAG, "SQLite exception when retrieving all " + klazz.simpleName + "s!")
            Toast.makeText(context, context.resources.getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show()
        }

        close()
        return result
    }

    protected fun getDao(): Dao<T, ID>? {
        try {
            //noinspection unchecked
            dao = OpenHelperManager.getHelper(context, Database::class.java).getDao(klazz)
            return dao
        } catch (e: SQLException) {
            Log.e(TAG, "SQLite exception when accessing " + klazz.simpleName + " database!")
            Toast.makeText(context, context.resources.getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show()
            return null
        }

    }

    protected val latest: T?
        get() {
            var entity: T? = null
            getDao()
            try {
                entity = dao!!.queryBuilder().orderBy(CommonColumns.DATE_COLUMN, false).limit(1L).queryForFirst()
            } catch (e: SQLException) {
                Log.e(TAG, "SQLite exception when accessing " + klazz.simpleName + " database!")
                Toast.makeText(context, context.resources.getString(R.string.error_retrieving_all_entities), Toast.LENGTH_SHORT).show()
            } finally {
                close()
            }
            return entity
        }

    protected fun close() {
        OpenHelperManager.releaseHelper()
    }

    companion object {
        private val TAG = BaseRepository::class.java.name
    }


}
