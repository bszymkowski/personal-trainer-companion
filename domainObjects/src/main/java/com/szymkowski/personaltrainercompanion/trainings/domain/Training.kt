package com.szymkowski.personaltrainercompanion.trainings.domain

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.szymkowski.personaltrainercompanion.core.BaseEntity

import org.joda.time.DateTime

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@DatabaseTable(tableName = Training.TABLE_NAME)
//todo undo public
class Training : BaseEntity {

    @DatabaseField(generatedId = true)
    var id: Long?
        set(id) {
            this.id = id
        }

    constructor(trainingDate: DateTime) : super(trainingDate) {
        this.id = 0L
    }

    companion object {
        val TABLE_NAME = "table_trainings"
    }

}
