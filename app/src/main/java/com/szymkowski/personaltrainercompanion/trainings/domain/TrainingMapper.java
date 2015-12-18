package com.szymkowski.personaltrainercompanion.trainings.domain;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    TrainingDTO trainingToTrainingDto(Training training);

    Training trainingDtoToTraining(TrainingDTO trainingDTO);
}
