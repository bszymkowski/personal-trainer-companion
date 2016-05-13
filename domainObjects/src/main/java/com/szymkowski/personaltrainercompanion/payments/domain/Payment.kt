package com.szymkowski.personaltrainercompanion.payments.domain

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
@DatabaseTable(tableName = Payment.TABLE_NAME)
//todo undo public
class Payment : BaseEntity {
    @DatabaseField(generatedId = true)
    var id: Long?
        set(id) {
            this.id = id
        }

    //    @DatabaseField(columnName = BaseEntity.DATE_COLUMN)
    //    private DateTime paymentDate;

    @DatabaseField(columnName = NUMBER_OF_CLASSES_PAID_COLUMN)
    var numberOfClassesPaid: Int
        set(numberOfClassesPaid) {
            this.numberOfClassesPaid = numberOfClassesPaid
        }

    constructor(paymentDate: DateTime, numberOfClassesPaid: Int) : super(paymentDate) {
        this.id = 0L
        //        this.paymentDate = paymentDate;
        this.numberOfClassesPaid = numberOfClassesPaid
    }

    companion object {
        val TABLE_NAME = "table_payments"
        val NUMBER_OF_CLASSES_PAID_COLUMN = "number_of_classes_paid"
    }

}
