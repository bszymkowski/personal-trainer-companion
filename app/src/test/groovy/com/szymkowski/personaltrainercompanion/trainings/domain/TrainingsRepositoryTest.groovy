package com.szymkowski.personaltrainercompanion.trainings.domain
import android.os.Build
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.core.Database
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider
import org.joda.time.DateTime
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowToast
import org.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsRepositoryTest extends GradleRoboSpecification {

    @Shared Dao<Training, Long> trainingsDao

    def setupSpec() {
        trainingsDao = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDomainDao(Training.class)
    }

    def setup() {
        trainingsDao.delete(trainingsDao.queryForAll())
    }

    def cleanup() {
        trainingsDao.delete(trainingsDao.queryForAll())
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }

    def 'should correctly obtain total number of classes paid for when no trainings have been used'() {
        given:
            def mockProvider = Mock(PaidNumberOfTrainingsProvider)
            def mockCallback = Mock(RepositoryCallback)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockCallback, mockProvider)
        when:
            def result = trainingRepo.numberOfTrainingsRemaining
        then:
            1* mockProvider.numberOfTrainingsPaidFor >> 42
            result == 42
    }


    def 'should correctly save training information'() {
        given:
            def mockProvider = Mock(PaidNumberOfTrainingsProvider)
            def mockCallback = Mock(RepositoryCallback)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockCallback, mockProvider)
            def date = new DateTime()
            def training = new TrainingDTO(date)
        when:
            mockProvider.getNumberOfTrainingsPaidFor() >> 5
            trainingRepo.addTraining(training)
            def result = trainingsDao.queryForAll()
            def resultTraining = result.iterator().next() as Training
        then:
            result.size() == 1
            resultTraining.getDate() == date
    }

    def 'should not add training when none available'() {
        given:
            def mockProvider = Mock(PaidNumberOfTrainingsProvider)
            def mockCallback = Mock(RepositoryCallback)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockCallback, mockProvider)
            mockProvider.numberOfTrainingsPaidFor >> 0
        when:
            trainingRepo.addTraining(new TrainingDTO(new DateTime()))
        then:
            ShadowToast.getTextOfLatestToast() == RuntimeEnvironment.application.getString(R.string.no_trainings_remaining)
            trainingsDao.queryForAll().size() == 0

    }

    def 'should show alert dialog if attempting to add second training on same date'() {
        given:
            def mockProvider = Mock(PaidNumberOfTrainingsProvider)
            def mockCallback = Mock(RepositoryCallback)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockCallback, mockProvider)
            mockProvider.getNumberOfTrainingsPaidFor() >> 5
            trainingsDao.create(new Training(new DateTime()))
            trainingsDao.create(new Training(new DateTime().minusDays(3)))
        when:
            def training = new TrainingDTO(new DateTime())
            trainingRepo.addTraining(training)
        then:
            ShadowAlertDialog.latestAlertDialog != null
    }
}
