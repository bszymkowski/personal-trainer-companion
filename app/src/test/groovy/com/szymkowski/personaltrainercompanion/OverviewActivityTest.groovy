package com.szymkowski.personaltrainercompanion

import android.app.AlertDialog
import android.os.Build
import android.widget.NumberPicker
import android.widget.TextView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.payments.addpayment.AddPaymentDialog
import com.szymkowski.personaltrainercompanion.payments.domain.Database
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowDialog
import pl.polidea.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class OverviewActivityTest extends GradleRoboSpecification {

    @Shared def paymentDAO;
    def controller;

    def setup() {
        paymentDAO.delete(paymentDAO.queryForAll())
        controller = Robolectric.buildActivity(OverviewActivity.class).create()
        controller.start()
        controller.resume()
        controller.visible()
    }

    def cleanup() {
        controller.pause()
        controller.destroy()
    }

    def setupSpec() {
        paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao()
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }


    def 'should display no last payment information if database empty'() {
        given:
            def overviewActivity = controller.get()
        when:
            TextView lastPaymentInfo = overviewActivity.findViewById(R.id.last_payment_info) as TextView
        then:
            lastPaymentInfo.getText() == RuntimeEnvironment.application.getResources().getString(R.string.no_payment_found)

    }

    def 'should call add payment dialog when add payment button is pressed and cancel properly'() {
        given:
            def overviewActivity = controller.get()
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
        then:
            dialog != null
            dialog.isShowing()
            dialog as AddPaymentDialog
            dialog.findViewById(R.id.add_payment_dialog_button_cancel) != null
        when:
            dialog.findViewById(R.id.add_payment_dialog_button_cancel).performClick()
        then:
            !dialog.isShowing()

    }

    def 'should call add payment dialog when add payment button is pressed and add payment'() {
        given:
            def overviewActivity = controller.get()
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
        then:
            dialog != null
            dialog.isShowing()
            dialog as AddPaymentDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok) != null
        when:
            def payment = new PaymentDTO(new DateTime(), (dialog.findViewById(R.id.add_payment_dialog_number_picker) as NumberPicker).getValue())
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            def dateFormatter = DateTimeFormat.shortDateTime();
            def dateTime = dateFormatter.print(payment.getPaymentDate())
        then:
            (overviewActivity.findViewById(R.id.last_payment_info) as TextView).getText() == String.format(RuntimeEnvironment.application.getResources().getString(R.string.last_payment_info_string), dateTime, payment.getNumberOfClassesPaid())

    }

    def 'should inform that payment was already added on this date and not add if "cancel" clicked'() {
        given:
            def overviewActivity = controller.get()
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            def confirmDialog = ShadowAlertDialog.latestAlertDialog
        then:
            confirmDialog != null
            confirmDialog.showing
        when:
            confirmDialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick()
        then:
            !confirmDialog.showing
            paymentDAO.queryForAll().size() == 1
    }

    def 'should inform that payment was already added on this date and add if "ok" clicked'() {
        given:
            def overviewActivity = controller.get()
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            def confirmDialog = ShadowAlertDialog.latestAlertDialog
        then:
            confirmDialog != null
            confirmDialog.showing
        when:
            confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            paymentDAO.queryForAll().size() == 2

    }
}
