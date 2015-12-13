package com.szymkowski.personaltrainercompanion.payments.addpayment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.szymkowski.personaltrainercompanion.R;
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO;

import org.joda.time.DateTime;

public class AddPaymentDialog extends Dialog {

    private static final String TAG = AddPaymentDialog.class.getSimpleName();

    //// TODO: 12.12.2015 get this to display add/remove buttons. I hate this layout
    private NumberPicker addPaymentNumberPicker;


    private AddPaymentDialogCallback callback;

    //// TODO: 12.12.2015 move this to preferences
    private static final int ADD_PAYMENT_MAX = 8;
    //// TODO: 12.12.2015 move this to preferences
    private static final int ADD_PAYMENT_DEFAULT = 8;

    public AddPaymentDialog(Context context, AddPaymentDialogCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_dialog);
        addPaymentNumberPicker = (NumberPicker) findViewById(R.id.add_payment_dialog_number_picker);
        addPaymentNumberPicker.setMinValue(0);
        addPaymentNumberPicker.setMaxValue(ADD_PAYMENT_MAX);
        addPaymentNumberPicker.setValue(ADD_PAYMENT_DEFAULT);

        Button positiveButton = (Button) findViewById(R.id.add_payment_dialog_button_ok);
        Button negativeButton = (Button) findViewById(R.id.add_payment_dialog_button_cancel);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDTO dto= new PaymentDTO(new DateTime(), addPaymentNumberPicker.getValue());
                callback.addPayment(dto);
                dismiss();
            }
        });

    }
}
