package com.szymkowski.personaltrainercompanion.trainings.domain;

import android.content.Context;

import com.szymkowski.personaltrainercompanion.core.BaseRepository;
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider;

public class TrainingsRepository extends BaseRepository<Training, Long> {

    private final PaidNumberOfTrainingsProvider provider;

    private static final String TAG = TrainingsRepository.class.getSimpleName();
    private final TrainingMapper trainingMapper = TrainingMapper.INSTANCE;

    public TrainingsRepository(Context context, PaidNumberOfTrainingsProvider provider) {
        super(context);
        this.provider = provider;
    }

    public int getNumberOfTrainingsRemaining() {
        int result = provider.getNumberOfTrainingsPaidFor();
        return result;
    }

    public void addTraining(TrainingDTO trainingDTO) {
        create(trainingMapper.trainingDtoToTraining(trainingDTO));
    }


}
