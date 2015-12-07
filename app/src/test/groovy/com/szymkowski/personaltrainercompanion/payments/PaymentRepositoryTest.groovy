package com.szymkowski.personaltrainercompanion.payments

import android.os.Build
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.OrmDbHelper
import com.szymkowski.personaltrainercompanion.payments.domain.db.Payment
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class PaymentRepositoryTest extends GradleRoboSpecification  {


    def paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), OrmDbHelper.class).getDao()
    def paymentRepository = new PaymentRepository(RuntimeEnvironment.application.getApplicationContext())

    def 'get last payment should return last payment dao'() {
        given:
            def targetDate = new Date()
            def payment = new Payment(targetDate, 8)
            def payment2 = new Payment(targetDate.minus(1), 7)
            def payment3 = new Payment(targetDate.minus(2), 6)
            def payment4 = new Payment(targetDate.minus(3), 5)
        when:
            paymentDAO.create(payment)
            paymentDAO.create(payment2)
            paymentDAO.create(payment3)
            paymentDAO.create(payment4)
            def resultPaymentDTO = paymentRepository.lastPayment
        then:
            resultPaymentDTO.numberOfClassesPaid == 8
            resultPaymentDTO.paymentDate == targetDate
    }


}
