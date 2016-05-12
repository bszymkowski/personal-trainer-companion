package com.szymkowski.personaltrainercompanion.trainings.domain

import org.joda.time.DateTime

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
class TrainingDTO {
    var id: Long?
        set(id) {
            this.id = id
        }
    var trainingDate: DateTime
        set(trainingDate) {
            this.trainingDate = trainingDate
        }

    constructor(trainingDate: DateTime) {
        this.id = 0L
        this.trainingDate = trainingDate
    }

}
