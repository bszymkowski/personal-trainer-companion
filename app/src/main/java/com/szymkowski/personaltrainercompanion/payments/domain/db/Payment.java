package com.szymkowski.personaltrainercompanion.payments.domain.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@DatabaseTable
public class Payment {
    public static final String PAYMENT_DATE_COLUMN = "payment_date";
    public static final String NUMBER_OF_CLASSES_PAID_COLUMN = "number_of_classes_paid";
    @DatabaseField(generatedId = true, id = true)
    private Long id;

    @DatabaseField(columnName = PAYMENT_DATE_COLUMN)
    private Date paymentDate;

    @DatabaseField(columnName = NUMBER_OF_CLASSES_PAID_COLUMN)
    private int numberOfClassesPaid;


}
