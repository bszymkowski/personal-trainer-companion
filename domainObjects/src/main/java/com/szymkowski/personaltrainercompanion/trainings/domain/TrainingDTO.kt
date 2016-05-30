package com.szymkowski.personaltrainercompanion.trainings.domain

import org.joda.time.DateTime

data class TrainingDTO (
        val id : Long,
        var trainingDate: DateTime
){



    constructor(trainingDate: DateTime) : this(0L, trainingDate)

    constructor() : this(0L, DateTime.now())

}
