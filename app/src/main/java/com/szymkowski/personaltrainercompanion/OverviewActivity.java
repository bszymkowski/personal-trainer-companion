package com.szymkowski.personaltrainercompanion;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.szymkowski.personaltrainercompanion.payments.PaymentDTO;
import com.szymkowski.personaltrainercompanion.payments.PaymentRepository;
import com.szymkowski.personaltrainercompanion.payments.addpayment.AddPaymentDialog;
import com.szymkowski.personaltrainercompanion.payments.addpayment.AddPaymentDialogCallback;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class OverviewActivity extends AppCompatActivity implements AddPaymentDialogCallback {

    private DateTimeFormatter dateTimeFormatter;
    private static final String TAG = OverviewActivity.class.getSimpleName();
    private TextView mLastPaymentInfoText;

    private PaymentRepository mPaymentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPaymentRepository = new PaymentRepository(this);
        mLastPaymentInfoText = (TextView) findViewById(R.id.last_payment_info);

        FloatingActionButton fabAddPayment = (FloatingActionButton) findViewById(R.id.fab_action_add_payment);
        fabAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog addPaymentDialog = new AddPaymentDialog(OverviewActivity.this);
                addPaymentDialog.show();
            }
        });
        dateTimeFormatter = DateTimeFormat.shortDateTime();

        Log.i(TAG, "Activity created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLastPayment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addPayment(PaymentDTO newPayment) {
        mPaymentRepository.addPayment(newPayment);
        updateLastPayment();
    }

    private void updateLastPayment() {
        PaymentDTO lastPaymentDto = mPaymentRepository.getLastPayment();
        String paymentInfoText;
        if (lastPaymentDto == null) {
            paymentInfoText = getResources().getString(R.string.no_payment_found);
        } else {
            String rawPaymentInfo = getResources().getString(R.string.last_payment_info_string);
            String dateTime = dateTimeFormatter.print(lastPaymentDto.getPaymentDate());
            paymentInfoText = String.format(rawPaymentInfo, dateTime, lastPaymentDto.getNumberOfClassesPaid());
        }
        mLastPaymentInfoText.setText(paymentInfoText);
    }
}
