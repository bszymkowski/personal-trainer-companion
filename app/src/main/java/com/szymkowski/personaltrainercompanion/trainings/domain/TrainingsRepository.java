package com.szymkowski.personaltrainercompanion.trainings.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
public class TrainingsRepository {
    private PaidNumberOfTrainingsProvider provider;

    public int getNumberOfClassesRemaining() {
        int result = provider.getNumberOfTrainingsPaidFor();
        return result;
    }

}