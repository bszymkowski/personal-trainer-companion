package com.szymkowski.personaltrainercompanion.trainings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.core.DateFormatter
import com.szymkowski.personaltrainercompanion.trainings.domain.Training
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingsRepository


class TrainingsAdapter(mContext : Context) : RecyclerView.Adapter<TrainingHolder>() {
    val trainingsRepo : TrainingsRepository = TrainingsRepository(mContext)

    val dateFormatter = DateFormatter.getDateFormatter(mContext.resources.getString(R.string.date_time_format))

    override fun getItemCount(): Int = trainingsRepo.count

    override fun onBindViewHolder(holder: TrainingHolder, position: Int) {
        val training : Training? = findTrainingForPosition(position)
        holder.trainingDate.text = dateFormatter.print(training?.date)


    }

    private fun findTrainingForPosition(position : Int) : Training? {
        val offset = 1
        return trainingsRepo.find(position.toLong()+offset)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TrainingHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.training_list_item, null)
        val holder = TrainingHolder(view);
        return holder
    }
}

class TrainingHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
    val trainingDate : TextView
    val trainingDay : TextView

    init {
        trainingDate = itemview.findViewById(R.id.training_date) as TextView
        trainingDay = itemview.findViewById(R.id.training_day_of_week) as TextView
    }

}
