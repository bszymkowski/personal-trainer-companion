package com.szymkowski.personaltrainercompanion.payments
import android.os.Build
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.payments.domain.dto.PaymentMapper
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class PaymentMapperTest extends GradleRoboSpecification  {

    def mapper = PaymentMapper.INSTANCE;

    def "should map Payment to PaymentDTO" () {
        given:
            def date = new Date()
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
            def date = new Date()
            int numberPaid = 5
            def payment;
            def paymentDTO = new PaymentDTO(date, numberPaid)
        when:
            payment = mapper.paymentDTOToPayment(paymentDTO)
        then:
            payment.numberOfClassesPaid == numberPaid
            payment.paymentDate == date
    }

}