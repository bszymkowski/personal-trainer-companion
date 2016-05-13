package com.szymkowski.personaltrainercompanion.payments.domain

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.szymkowski.personaltrainercompanion.core.CommonColumns
import org.joda.time.DateTime


@DatabaseTable(tableName = Payment.TABLE_NAME)
data class Payment (@DatabaseField(generatedId = true) val id : Long) {

    @DatabaseField(columnName = NUMBER_OF_CLASSES_PAID_COLUMN)
    var numberOfClassesPaid: Int = 0

    @DatabaseField(columnName = CommonColumns.DATE_COLUMN)
    lateinit var paymentDate : DateTime

    constructor(paymentDate: DateTime, numberOfClassesPaid: Int) : this(0L) {
        this.paymentDate = paymentDate;
        this.numberOfClassesPaid = numberOfClassesPaid
    }

    constructor() : this(0L)

    companion object {
        const val TABLE_NAME = "table_payments"
        const val NUMBER_OF_CLASSES_PAID_COLUMN = "number_of_classes_paid"
    }

}
