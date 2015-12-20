package com.szymkowski.personaltrainercompanion.payments.domain
import android.database.sqlite.SQLiteException
import android.os.Build
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.core.Database

import org.joda.time.DateTime
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class PaymentRepositoryTest extends GradleRoboSpecification  {


    @Shared def paymentDAO
    @Shared PaymentRepository paymentRepository

    def setup() {
        paymentDAO.delete(paymentDAO.queryForAll())
        ShadowAlertDialog.reset()
    }

    def setupSpec() {
        paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Payment.class)
        paymentRepository = new PaymentRepository(RuntimeEnvironment.application.getApplicationContext())
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }

    def 'get last payment should return last payment dao'() {
        given:

            def targetDate = new DateTime()
            def payment = new Payment(targetDate, 8)
            def payment2 = new Payment(targetDate.minus(1), 7)
            def payment3 = new Payment(targetDate.minus(2), 6)
            def payment4 = new Payment(targetDate.minus(3), 5)
        when:
            paymentDAO.create(payment)
            paymentDAO.create(payment2)
            paymentDAO.create(payment3)
            paymentDAO.create(payment4)
            def resultPaymentDTO = paymentRepository.getLastPaymentDTO()
        then:
            resultPaymentDTO.numberOfClassesPaid == 8
            resultPaymentDTO.paymentDate == targetDate
    }

    //fixme use mocks
    def 'should properly receive DTO and save payment'() {
        given:
            def date = new DateTime();
            def payment = new PaymentDTO(date, 8)
        when:
            paymentRepository.addPayment(payment)
        then:
            def payment1 = paymentDAO.findAll().iterator().next()
            payment1.paymentDate == payment.paymentDate
            payment1.numberOfClassesPaid == payment.numberOfClassesPaid
    }

    def 'should call callback when payment with same date is already added'() {
        given:
            def paymentRepository = new PaymentRepository(RuntimeEnvironment.application.getApplicationContext())
            def payment = new PaymentDTO(new DateTime(), 8)
            def payment2 = new PaymentDTO(new DateTime(), 8)
            paymentRepository.addPayment(payment)
        when:
            paymentRepository.addPayment(payment2)
        then:
            ShadowAlertDialog.latestAlertDialog != null
    }

    def 'should not call callback when payment with different date is already added'() {
        given:
            def paymentRepository = new PaymentRepository(RuntimeEnvironment.application.getApplicationContext())
            def payment = new PaymentDTO(new DateTime(), 8)
            def payment2 = new PaymentDTO(new DateTime().plusDays(1), 8)
            paymentRepository.addPayment(payment)
        when:
            paymentRepository.addPayment(payment2)
        then:
            ShadowAlertDialog.latestAlertDialog == null


    }

    def 'should correctly obtain total number of classes paid for'() {
        given:
            def payment1 = new PaymentDTO(new DateTime(), 5)
            def payment2 = new PaymentDTO(new DateTime().minusWeeks(1), 8)
            def payment3 = new PaymentDTO(new DateTime().minusWeeks(2), 8)
            paymentRepository.addPayment(payment1)
            paymentRepository.addPayment(payment2)
            paymentRepository.addPayment(payment3)
        when:
            int result = paymentRepository.numberOfTrainingsPaidFor
        then:
            notThrown(SQLiteException)
            result == 21

    }


}
