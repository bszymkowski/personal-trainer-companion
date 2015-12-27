package com.szymkowski.personaltrainercompanion.trainings.domain;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.core.Database;

import org.joda.time.DateTime;
import org.robolectric.RuntimeEnvironment;

import java.sql.SQLException;

public class TrainingsDaoHelper {

    public static Dao<Training, Long> getTrainingsDao() throws SQLException {
        return OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Training.class);
    }

    public static void addTraining() throws SQLException {
        Dao<Training, Long> dao = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Training.class);
        dao.create(new Training(new DateTime()));
        OpenHelperManager.releaseHelper();
    }
}
