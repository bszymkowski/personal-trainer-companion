package com.szymkowski.personaltrainercompanion.trainings.domain

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.core.BaseRepository
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider
import org.joda.time.DateTime
import org.joda.time.DateTimeComparator
import java.sql.SQLException

class TrainingsRepository(context: Context, callback: RepositoryCallback, private val provider: PaidNumberOfTrainingsProvider) : BaseRepository<Training, Long>(context, callback) {

    val numberOfTrainingsRemaining: Int
        get() = provider.getNumberOfTrainingsPaidFor()- count

    val latestTrainingDate: DateTime?
        get() {
            val training = latest
            return training?.date
        }

    fun addTraining(trainingDTO: TrainingDTO) {
        if (numberOfTrainingsRemaining < 1) {
            Toast.makeText(context, R.string.no_trainings_remaining, Toast.LENGTH_SHORT).show()
            return
        }
        if (isLastTrainingToday) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.resources.getString(R.string.training_added_today_title))
            builder.setMessage(context.resources.getString(R.string.training_added_today_message))
            builder.setNegativeButton(android.R.string.no) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                create(TrainingMapper.trainingDtoToTraining(trainingDTO))
                dialog.dismiss()
            }
            builder.show()
        } else {
            create(TrainingMapper.trainingDtoToTraining(trainingDTO))
        }
    }

    private val count: Int
        get() {
            var result = 0
            val dao = getDao()
            try {
                result = dao?.countOf()!!.toInt()
            } catch (e: SQLException) {
                Log.e(TAG, "SQLite exception when attempting to count trainings!")
                Toast.makeText(context, context.resources.getString(R.string.error_retrieving_entity), Toast.LENGTH_SHORT).show()
            }

            close()
            return result
        }

    private val isLastTrainingToday: Boolean
        get() {
            val training = latest
            return training != null && DateTimeComparator.getDateOnlyInstance().compare(DateTime(), training.date) == 0
        }

    companion object {

        private val TAG = TrainingsRepository::class.java.simpleName
    }

}
