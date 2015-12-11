package com.szymkowski.personaltrainercompanion.payments;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@DatabaseTable
class Payment {
    public static final String PAYMENT_DATE_COLUMN = "payment_date";
    public static final String NUMBER_OF_CLASSES_PAID_COLUMN = "number_of_classes_paid";
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = PAYMENT_DATE_COLUMN)
    private Date paymentDate;

    @DatabaseField(columnName = NUMBER_OF_CLASSES_PAID_COLUMN)
    private int numberOfClassesPaid;

    public Payment(Date paymentDate, int numberOfClassesPaid) {
        this.id = 0L;
        this.paymentDate = paymentDate;
        this.numberOfClassesPaid = numberOfClassesPaid;
    }

}
