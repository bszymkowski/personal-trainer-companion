package com.szymkowski.personaltrainercompanion.trainings.domain;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.core.BaseRepository;
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider;

import java.sql.SQLException;

public class TrainingsRepository extends BaseRepository<Training, Long> {

    private final PaidNumberOfTrainingsProvider provider;

    private static final String TAG = TrainingsRepository.class.getSimpleName();
    private final TrainingMapper trainingMapper = TrainingMapper.INSTANCE;

    public TrainingsRepository(Context context, PaidNumberOfTrainingsProvider provider) {
        super(context);
        this.provider = provider;
    }

    public int getNumberOfTrainingsRemaining() {
        int result = provider.getNumberOfTrainingsPaidFor() - getCount();
        return result;
    }

    public void addTraining(TrainingDTO trainingDTO) {
        create(trainingMapper.trainingDtoToTraining(trainingDTO));
    }

    private int getCount() {
        int result = 0;
        Dao<Training, Long> dao = getDao();
        try {
            result = (int) dao.countOf();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when attempting to count trainings!");
        }
        close();
        return result;
    }

}
