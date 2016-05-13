package com.szymkowski.personaltrainercompanion.trainings.domain

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.szymkowski.personaltrainercompanion.core.CommonColumns
import org.joda.time.DateTime

@DatabaseTable(tableName = Training.TABLE_NAME)
data class Training (@DatabaseField(generatedId = true) val id : Long){

    @DatabaseField(columnName = CommonColumns.DATE_COLUMN)
    lateinit var date : DateTime

    constructor(trainingDate: DateTime) : this(0L) {
        this.date = trainingDate
    }

    constructor() : this(0L)

    companion object {
        const val TABLE_NAME = "table_trainings"
    }

}
