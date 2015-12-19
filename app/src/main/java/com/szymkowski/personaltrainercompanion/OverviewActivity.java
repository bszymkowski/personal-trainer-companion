package com.szymkowski.personaltrainercompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialog;
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialogCallback;
import com.szymkowski.personaltrainercompanion.payments.RepositoryCallback;
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO;
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentRepository;
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingsRepository;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class OverviewActivity extends AppCompatActivity implements AddPaymentDialogCallback, RepositoryCallback {

    private static final String TAG = OverviewActivity.class.getSimpleName();
    private TextView mLastPaymentInfoText;

    private PaymentRepository mPaymentRepository;
    private TrainingsRepository mTrainingsRepository;
    private TextView mNumberOfTrainingsInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPaymentRepository = new PaymentRepository(this, this);
        mLastPaymentInfoText = (TextView) findViewById(R.id.last_payment_info);

        mTrainingsRepository = new TrainingsRepository(this, mPaymentRepository);
        mNumberOfTrainingsInfoText = (TextView) findViewById(R.id.number_of_classes_remaining);

        final FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fabAddPayment = (FloatingActionButton) findViewById(R.id.fab_action_add_payment);
        fabAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog addPaymentDialog = new AddPaymentDialog(OverviewActivity.this, OverviewActivity.this);
                addPaymentDialog.show();
                floatingActionsMenu.collapse();
            }
        });

        Log.i(TAG, "Activity created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLastPayment();
        updateNumberOfTrainingsRemaining();
    }

    private void updateNumberOfTrainingsRemaining() {
        mNumberOfTrainingsInfoText.setText(String.format(getResources().getString(R.string.number_format_string) ,mTrainingsRepository.getNumberOfTrainingsRemaining()));
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
        PaymentDTO lastPaymentDto = mPaymentRepository.getLastPaymentDTO();
        String paymentInfoText;
        if (lastPaymentDto == null) {
            paymentInfoText = getResources().getString(R.string.no_payment_found);
        } else {
            String rawPaymentInfo = getResources().getString(R.string.last_payment_info_string);
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(getResources().getString(R.string.date_time_format)).withLocale(Locale.getDefault());
            String dateTime = dateTimeFormatter.print(lastPaymentDto.getPaymentDate());
            paymentInfoText = String.format(rawPaymentInfo, dateTime, lastPaymentDto.getNumberOfClassesPaid());
        }
        mLastPaymentInfoText.setText(paymentInfoText);
        updateNumberOfTrainingsRemaining();
    }

    @Override
    public void onPaymentAlreadyAdded(final PaymentDTO paymentDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.payment_already_added_today_title);
        builder.setMessage(R.string.payment_already_added_today_message);
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPaymentRepository.addPaymentWhenSameDateExists(paymentDTO);
                updateLastPayment();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
