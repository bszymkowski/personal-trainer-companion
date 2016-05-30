package com.szymkowski.personaltrainercompanion.trainings.domain


object TrainingMapper {

    fun trainingToTrainingDto(training: Training): TrainingDTO {
        val (id, date) = training
        return TrainingDTO(id, date)
    }


    fun trainingDtoToTraining(trainingDTO: TrainingDTO): Training {
        val (id, date) = trainingDTO
        return Training(id, date)
    }


}
