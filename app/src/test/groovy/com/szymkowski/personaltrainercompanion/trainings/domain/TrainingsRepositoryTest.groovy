package com.szymkowski.personaltrainercompanion.trainings.domain

import android.os.Build
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.core.Database
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider
import org.joda.time.DateTime
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsRepositoryTest extends GradleRoboSpecification {

    @Shared def trainingsDao

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
            def mockprovider = Mock(PaidNumberOfTrainingsProvider)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockprovider)
        when:
            def result = trainingRepo.numberOfTrainingsRemaining
        then:
            1* mockprovider.numberOfTrainingsPaidFor >> 42
            result == 42
    }


    def 'should correctly save training information'() {
        given:
            def mockProvider = Mock(PaidNumberOfTrainingsProvider)
            def trainingRepo = new TrainingsRepository(RuntimeEnvironment.application.getApplicationContext(), mockProvider)
            def date = new DateTime()
            def training = new TrainingDTO(date)
        when:
            trainingRepo.addTraining(training)
            def result = trainingsDao.queryForAll()
            def resultTraining = result.iterator().next() as Training
        then:
            result.size() == 1
            resultTraining.trainingDate == date


    }
}
