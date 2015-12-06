package com.szymkowski.personaltrainercompanion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.szymkowski.personaltrainercompanion.payments.PaymentRepository;
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentDTO;

public class Overview extends OrmLiteBaseActivity {

    private PaymentRepository paymentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        paymentRepository = new PaymentRepository(getConnectionSource());

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
        TextView lastPayment = (TextView) findViewById(R.id.last_payment_info);
        PaymentDTO lastPaymentInfo = paymentRepository.getLastPayment();
        String paymentInfoText;
        if (lastPaymentInfo == null) {
            paymentInfoText = getResources().getString(R.string.no_payment_found);
        } else {
            String rawPaymentInfo = getResources().getString(R.string.last_payment_info_string);
            paymentInfoText = String.format(rawPaymentInfo, lastPaymentInfo.getPaymentDate().toString(), lastPaymentInfo.getNumberOfClassesPaid());
        }
        lastPayment.setText(paymentInfoText);
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
