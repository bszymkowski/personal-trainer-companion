package com.szymkowski.personaltrainercompanion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.szymkowski.personaltrainercompanion.payments.PaymentDTO;
import com.szymkowski.personaltrainercompanion.payments.PaymentRepository;

public class OverviewActivity extends AppCompatActivity {

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PaymentDTO lastPaymentDto = mPaymentRepository.getLastPayment();
        String paymentInfoText;
        if (lastPaymentDto == null) {
            paymentInfoText = getResources().getString(R.string.no_payment_found);
        } else {
            String rawPaymentInfo = getResources().getString(R.string.last_payment_info_string);
            paymentInfoText = String.format(rawPaymentInfo, lastPaymentDto.getPaymentDate().toString(), lastPaymentDto.getNumberOfClassesPaid());
        }
        mLastPaymentInfoText.setText(paymentInfoText);
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
}
