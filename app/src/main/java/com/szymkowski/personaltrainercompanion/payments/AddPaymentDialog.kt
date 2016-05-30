package com.szymkowski.personaltrainercompanion.payments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO
import org.joda.time.DateTime

class AddPaymentDialog(context: Context, private val callback: AddPaymentDialogCallback) : Dialog(context) {

    //// TODO: 12.12.2015 get this to display add/remove buttons. I hate this layout
    private lateinit var addPaymentNumberPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_payment_dialog)
        addPaymentNumberPicker = findViewById(R.id.add_payment_dialog_number_picker) as NumberPicker
        addPaymentNumberPicker.minValue = 0
        addPaymentNumberPicker.maxValue = ADD_PAYMENT_MAX
        addPaymentNumberPicker.value = ADD_PAYMENT_DEFAULT


        val positiveButton = findViewById(R.id.add_payment_dialog_button_ok) as Button
        val negativeButton = findViewById(R.id.add_payment_dialog_button_cancel) as Button

        negativeButton.setOnClickListener {
            Log.i(TAG, "Dialog cancelled")
            dismiss()
        }

        positiveButton.setOnClickListener {
            val dto = PaymentDTO(DateTime(), addPaymentNumberPicker.value)
            callback.addPayment(dto)
            Log.i(TAG, "Payment added, date: " + dto.paymentDate.toString() + "for " + dto.numberOfClassesPaid + "classes.")
            dismiss()
        }
        Log.i(TAG, "Dialog created")

    }


    companion object {

        private val TAG = AddPaymentDialog::class.java.simpleName

        //// TODO: 12.12.2015 move this to preferences
        private val ADD_PAYMENT_MAX = 8
        //// TODO: 12.12.2015 move this to preferences
        private val ADD_PAYMENT_DEFAULT = 8
    }
}
