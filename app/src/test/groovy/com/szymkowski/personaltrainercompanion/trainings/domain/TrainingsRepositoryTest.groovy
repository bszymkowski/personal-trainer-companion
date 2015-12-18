package com.szymkowski.personaltrainercompanion.trainings.domain

import android.os.Build
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.trainings.providers.PaidNumberOfTrainingsProvider
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsRepositoryTest extends GradleRoboSpecification {

    def 'should correctly obtain total number of classes paid for when no trainings have been used'() {
        given:
            def mockprovider = Mock(PaidNumberOfTrainingsProvider)
            def trainingRepo = new TrainingsRepository(mockprovider)
        when:
            def result = trainingRepo.numberOfClassesRemaining
        then:
            1* mockprovider.numberOfTrainingsPaidFor >> 42
            result == 42
    }

}
