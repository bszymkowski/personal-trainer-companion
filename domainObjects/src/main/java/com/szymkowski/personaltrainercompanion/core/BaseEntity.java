package com.szymkowski.personaltrainercompanion.core;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public abstract class BaseEntity {

    public static final String DATE_COLUMN = "date";

    @DatabaseField(columnName = BaseEntity.DATE_COLUMN)
    protected DateTime date;

}
