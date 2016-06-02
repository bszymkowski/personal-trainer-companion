package com.szymkowski.personaltrainercompanion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.szymkowski.personaltrainercompanion.core.DateFormatter
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialog
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialogCallback
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentRepository
import com.szymkowski.personaltrainercompanion.trainings.TrainingsActivity
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingDTO
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingsRepository
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.android.synthetic.main.content_overview.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

class OverviewActivity : AppCompatActivity(), AddPaymentDialogCallback, RepositoryCallback {


    lateinit private var mPaymentRepository: PaymentRepository
    lateinit private var mTrainingsRepository: TrainingsRepository
    lateinit private var dateTimeFormatter: DateTimeFormatter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(toolbar)

        dateTimeFormatter = DateFormatter.getDateFormatter(resources.getString(R.string.date_time_format))

        mPaymentRepository = PaymentRepository(this, this)

        mTrainingsRepository = TrainingsRepository(this, this, mPaymentRepository)


        fab_action_add_payment.setOnClickListener({
            val addPaymentDialog = AddPaymentDialog(this@OverviewActivity, this@OverviewActivity)
            addPaymentDialog.show()
            fab_menu.collapse()
        })

        fab_action_add_training.setOnClickListener {
            val builder = AlertDialog.Builder(this@OverviewActivity)
            builder.setTitle(R.string.add_single_training_title)
            builder.setMessage(R.string.add_single_training_message)
            builder.setNegativeButton(android.R.string.no) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                mTrainingsRepository.addTraining(TrainingDTO(DateTime()))
                fab_menu.collapse()
                dialog.dismiss()
            }
            builder.show()
        }
        last_training_info_date.setOnClickListener {
            val intent = Intent(this@OverviewActivity, TrainingsActivity::class.java)
            startActivity(intent)

        }

        Log.i(TAG, "Activity created")
    }

    override fun onResume() {
        super.onResume()
        updateLastPayment()
        updateNumberOfTrainingsRemaining()
        updateLastTrainingInfo()

    }

    private fun updateLastTrainingInfo() {
        val dateTime = mTrainingsRepository.latestTrainingDate
        if (dateTime != null) {
            last_training_info_date.text = dateTimeFormatter.print(dateTime)
        }
    }

    private fun updateNumberOfTrainingsRemaining() {
        val trainingsRemaining = mTrainingsRepository.numberOfTrainingsRemaining
        number_of_trainings_remaining.text = trainingsRemaining.toString()

        if (trainingsRemaining > 0) {
            fab_action_add_training.visibility = View.VISIBLE
        } else {
            fab_action_add_training.visibility = View.GONE
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_overview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun addPayment(newPayment: PaymentDTO) {
        mPaymentRepository.addPayment(newPayment)
    }

    private fun updateLastPayment() {
        val lastPaymentDto = mPaymentRepository.lastPaymentDTO
        val paymentInfoText: String
        if (lastPaymentDto == null) {
            paymentInfoText = resources.getString(R.string.no_data_found)
        } else {
            val rawPaymentInfo = resources.getString(R.string.last_payment_info_string)
            val dateTime = dateTimeFormatter.print(lastPaymentDto.paymentDate)
            paymentInfoText = String.format(rawPaymentInfo, dateTime, lastPaymentDto.numberOfClassesPaid)
        }
        last_payment_info.text = paymentInfoText
        updateNumberOfTrainingsRemaining()
    }

    override fun onDatasetChanged() {
        updateLastPayment()
        updateNumberOfTrainingsRemaining()
        updateLastTrainingInfo()

    }

    companion object {

        private val TAG = OverviewActivity::class.java.simpleName
    }
}
