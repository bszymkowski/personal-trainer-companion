package com.szymkowski.personaltrainercompanion.trainings.domain

import org.joda.time.DateTime

data class TrainingDTO (val id : Long){

    lateinit var trainingDate: DateTime

    constructor(trainingDate: DateTime) : this(0L) {
        this.trainingDate = trainingDate
    }

    constructor() : this(0L)

}
