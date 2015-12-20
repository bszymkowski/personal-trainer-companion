package com.szymkowski.personaltrainercompanion.trainings.domain

import android.os.Build
import com.szymkowski.personaltrainercompanion.BuildConfig
import org.joda.time.DateTime
import org.robolectric.annotation.Config
import org.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsMapperTest extends GradleRoboSpecification {

    def 'should map Training to TrainingDTO'() {
        given:
            def date = new DateTime()
            def training = new Training(0L, date)
        when:
            def trainingDTO = TrainingMapper.INSTANCE.trainingToTrainingDto(training)
        then:
            trainingDTO.trainingDate == training.trainingDate
    }

    def 'should map TrainingDTO to Training'() {
        given:
            def date = new DateTime()
            def trainingDTO = new TrainingDTO(date)
        when:
            def training = TrainingMapper.INSTANCE.trainingDtoToTraining(trainingDTO)
        then:
            trainingDTO.trainingDate == training.trainingDate
    }


}