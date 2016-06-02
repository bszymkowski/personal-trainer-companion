package com.szymkowski.personaltrainercompanion.trainings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.szymkowski.personaltrainercompanion.R
import kotlinx.android.synthetic.main.activity_trainings.*

class TrainingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainings)
        val adapter = TrainingsAdapter(this)
        trainings_overview.layoutManager = LinearLayoutManager(this)
        trainings_overview.adapter = adapter
    }
}
