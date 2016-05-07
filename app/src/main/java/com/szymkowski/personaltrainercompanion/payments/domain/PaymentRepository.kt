package com.szymkowski.personaltrainercompanion.payments.domain

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.core.BaseRepository
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider
import org.joda.time.DateTimeComparator
import java.sql.SQLException


class PaymentRepository(context: Context, callback: RepositoryCallback) : BaseRepository<Payment, Long>(context, callback), PaidNumberOfTrainingsProvider {
    private val paymentMapper = PaymentMapper.INSTANCE

    fun addPayment(paymentDTO: PaymentDTO) {
        val payment = paymentMapper.paymentDTOToPayment(paymentDTO)
        val previous = latest
        if (isPaymentOnSameDay(payment, previous)) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.payment_already_added_today_title)
            builder.setMessage(R.string.payment_already_added_today_message)
            builder.setNegativeButton(android.R.string.no) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                create(paymentMapper.paymentDTOToPayment(paymentDTO))
                dialog.dismiss()
            }
            builder.show()
        } else {
            create(paymentMapper.paymentDTOToPayment(paymentDTO))
        }
    }

    private fun isPaymentOnSameDay(current: Payment, previous: Payment?): Boolean {
        return previous != null && DateTimeComparator.getDateOnlyInstance().compare(current.getDate(), previous.getDate()) == 0
    }

    val lastPaymentDTO: PaymentDTO?
        get() {
            val lastPayment = latest
            return paymentMapper.paymentToPaymentDTO(lastPayment)
        }

    override fun getNumberOfTrainingsPaidFor(): Int {
        val paymentLongDao = getDao()
        var result = 0
        if (paymentLongDao != null) {
            try {
                val allPayments = paymentLongDao.queryForAll()
                for (payment in allPayments!!) {
                    result += payment.numberOfClassesPaid
                }
            } catch (e: SQLException) {
                Log.e(TAG, "SQLite exception in counting total number of classes paid. Exception: " + e.message)
            }
        }

        close()
        return result
    }

    companion object {

        private val TAG = PaymentRepository::class.java.simpleName
    }
}
