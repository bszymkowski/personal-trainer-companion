package com.szymkowski.personaltrainercompanion.payments.domain
import android.os.Build
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.BuildConfig
import org.joda.time.DateTime
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class PaymentRepositoryTest extends GradleRoboSpecification  {


    @Shared def paymentDAO
    @Shared def paymentRepository

    def setup() {
        paymentDAO.delete(paymentDAO.queryForAll())
    }

    def setupSpec() {
        paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao()
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
            def resultPaymentDTO = paymentRepository.lastPayment
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


}
