package com.szymkowski.personaltrainercompanion.trainings.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@DatabaseTable(tableName = Training.TABLE_NAME)
class Training {
    public static final String TABLE_NAME = "table_trainings";
    public static final String TRAINING_DATE_COLUMN = "training_date";
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = TRAINING_DATE_COLUMN)
    private DateTime trainingDate;

    public Training(DateTime trainingDate) {
        this.id = 0L;
        this.trainingDate = trainingDate;
    }

}
