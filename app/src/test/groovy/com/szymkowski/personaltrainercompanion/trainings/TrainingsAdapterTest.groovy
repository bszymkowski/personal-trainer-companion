package com.szymkowski.personaltrainercompanion.trainings
import android.os.Build
import android.support.v7.widget.RecyclerView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.core.Database
import com.szymkowski.personaltrainercompanion.trainings.domain.Training
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robospock.GradleRoboSpecification
import spock.lang.Shared
import spock.lang.Subject

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsAdapterTest extends GradleRoboSpecification {

    @Subject
    TrainingsAdapter trainingsAdapter

    @Shared Dao<Training, Long> trainingDAO;


    def setupSpec() {
        trainingDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Training.class);
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }

    def setup() {
        trainingDAO.delete(trainingDAO.queryForAll())
        trainingsAdapter = new TrainingsAdapter(RuntimeEnvironment.application.getApplicationContext())
    }

    def cleanup() {
        trainingDAO.delete(trainingDAO.queryForAll())
    }

    def 'should correctly count trainings'() {
        when:
            add 3
        then:
            confirm trainingsAdapter has 3
    }


    def add(number) {
        number.times {
            trainingDAO.create(new Training())

        }
    }


    def confirm(RecyclerView.Adapter adapter) {
            [has: { number ->
                    assert (adapter.itemCount == number)
                    return adapter.itemCount == number
            }]
    }



}
