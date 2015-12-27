package com.szymkowski.personaltrainercompanion.trainings.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);


    @Mappings({
            @Mapping(source = "date", target = "trainingDate"),
    })
    TrainingDTO trainingToTrainingDto(Training training);

    @Mappings({
            @Mapping(source = "trainingDate", target = "date"),
    })
    Training trainingDtoToTraining(TrainingDTO trainingDTO);
}
