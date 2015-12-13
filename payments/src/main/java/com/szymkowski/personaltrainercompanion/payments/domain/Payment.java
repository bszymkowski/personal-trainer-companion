package com.szymkowski.personaltrainercompanion.payments.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@DatabaseTable
class Payment {
    public static final String PAYMENT_DATE_COLUMN = "payment_date";
    public static final String NUMBER_OF_CLASSES_PAID_COLUMN = "number_of_classes_paid";
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = PAYMENT_DATE_COLUMN)
    private DateTime paymentDate;

    @DatabaseField(columnName = NUMBER_OF_CLASSES_PAID_COLUMN)
    private int numberOfClassesPaid;

    public Payment(DateTime paymentDate, int numberOfClassesPaid) {
        this.id = 0L;
        this.paymentDate = paymentDate;
        this.numberOfClassesPaid = numberOfClassesPaid;
    }

}
