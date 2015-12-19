package com.szymkowski.personaltrainercompanion.payments.addpayment

import android.os.Build
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialog
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialogCallback
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog
import org.robospock.GradleRoboSpecification


@Config(constants = BuildConfig, sdk= Build.VERSION_CODES.LOLLIPOP)
class AddPaymentDialogTest extends GradleRoboSpecification {

    def 'should call callback method addPayment when payment added'() {
        given:
            def dialogCallback = Mock(AddPaymentDialogCallback)
            AddPaymentDialog dialog = new AddPaymentDialog(RuntimeEnvironment.application.getApplicationContext(), dialogCallback)
        when:
            dialog.show()
        then:
            ShadowDialog.latestDialog as AddPaymentDialog
        when:
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
        then:
            1* dialogCallback.addPayment(_ as PaymentDTO)
    }

    def 'should not call callback payment adding cancelled'() {
        given:
            def dialogCallback = Mock(AddPaymentDialogCallback)
            AddPaymentDialog dialog = new AddPaymentDialog(RuntimeEnvironment.application.getApplicationContext(), dialogCallback)
        when:
            dialog.show()
        then:
            ShadowDialog.latestDialog as AddPaymentDialog
        when:
            dialog.findViewById(R.id.add_payment_dialog_button_cancel).performClick()
        then:
            0* dialogCallback.addPayment(_ as PaymentDTO)
    }

}
