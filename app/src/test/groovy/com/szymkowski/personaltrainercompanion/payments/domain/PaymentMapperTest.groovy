package com.szymkowski.personaltrainercompanion.payments.domain

import android.os.Build
import com.szymkowski.personaltrainercompanion.BuildConfig
import org.joda.time.DateTime
import org.robolectric.annotation.Config
import org.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class PaymentMapperTest extends GradleRoboSpecification  {

    def mapper = PaymentMapper.INSTANCE;

    def "should map Payment to PaymentDTO" () {
        given:
            def date = new DateTime()
            int numberPaid = 5
            def payment = new Payment(date, numberPaid)
            def paymentDTO
        when:
            paymentDTO = mapper.paymentToPaymentDTO(payment)
        then:
            paymentDTO.numberOfClassesPaid == numberPaid
            paymentDTO.paymentDate == date
    }

    def 'should map PaymentDTO to Payment' () {
        given:
            def date = new DateTime()
            int numberPaid = 5
            def payment;
            def paymentDTO = new PaymentDTO(date, numberPaid)
        when:
            payment = mapper.paymentDTOToPayment(paymentDTO)
        then:
            payment.numberOfClassesPaid == numberPaid
            payment.date == date
    }

}