package com.szymkowski.personaltrainercompanion.trainings.domain;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TrainingDTO {

    private DateTime trainingDate;
}
