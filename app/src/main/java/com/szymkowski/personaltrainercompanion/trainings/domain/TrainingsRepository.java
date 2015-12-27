package com.szymkowski.personaltrainercompanion.trainings.domain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.szymkowski.personaltrainercompanion.R;
import com.szymkowski.personaltrainercompanion.core.BaseRepository;
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

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

    public DateTime getLatestTrainingDate() {
        Training training = getLatest();
        return training == null ? null : training.getDate();
    }

    public void addTraining(final TrainingDTO trainingDTO) {
        if (isLastTrainingToday()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.training_added_today_title));
            builder.setMessage(context.getResources().getString(R.string.training_added_today_message));
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    create(trainingMapper.trainingDtoToTraining(trainingDTO));
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            create(trainingMapper.trainingDtoToTraining(trainingDTO));
        }
    }

    private int getCount() {
        int result = 0;
        Dao<Training, Long> dao = getDao();
        try {
            result = (int) dao.countOf();
        } catch (SQLException e) {
            Log.e(TAG, "SQLite exception when attempting to count trainings!");
            Toast.makeText(context, context.getResources().getString(R.string.error_retrieving_entity), Toast.LENGTH_SHORT).show();
        }
        close();
        return result;
    }

    private boolean isLastTrainingToday() {
        Training training = getLatest();
        return training != null && DateTimeComparator.getDateOnlyInstance().compare(new DateTime(), training.getDate()) == 0;
    }

}
