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
        int result = provider.getNumberOfTrainingsPaidFor();
        return result;
    }

    public void addTraining(TrainingDTO trainingDTO) {
        Training training = trainingMapper.trainingDtoToTraining(trainingDTO);
        try {
            Dao<Training, Long> trainingLongDao = getDao();
            trainingLongDao.create(training);
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception in adding training data. Exception: " + e.getMessage());
        } finally {
            close();
        }
    }


}
