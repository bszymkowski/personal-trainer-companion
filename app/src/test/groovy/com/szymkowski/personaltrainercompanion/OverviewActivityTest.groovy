package com.szymkowski.personaltrainercompanion
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.core.RepositoryCallback
import com.szymkowski.personaltrainercompanion.payments.AddPaymentDialog
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDTO
import com.szymkowski.personaltrainercompanion.payments.domain.PaymentDaoHelper
import com.szymkowski.personaltrainercompanion.trainings.TrainingsActivity
import com.szymkowski.personaltrainercompanion.trainings.domain.TrainingsDaoHelper
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowDialog
import org.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.LOLLIPOP)
class OverviewActivityTest extends GradleRoboSpecification {

    @Shared def paymentDAO;
    @Shared def trainingDAO;
    def controller;

    def setup() {
        paymentDAO.delete(paymentDAO.queryForAll())
        trainingDAO.delete(trainingDAO.queryForAll())
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
        trainingDAO = TrainingsDaoHelper.getTrainingsDao()
        paymentDAO = PaymentDaoHelper.getPaymentsDao()
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
        OpenHelperManager.releaseHelper()
    }

    def 'should not display add training menu item if no trainings are available'() {
        given:
            OverviewActivity overviewActivity = controller.get()
        when:
            overviewActivity.findViewById(R.id.fab_expand_menu_button).performClick()
            def addTraining = overviewActivity.findViewById(R.id.fab_action_add_training)
        then:
            addTraining.visibility == View.GONE

    }


    def 'should display no last payment information if database empty'() {
        given:
            def overviewActivity = controller.get()
        when:
            TextView lastPaymentInfo = overviewActivity.findViewById(R.id.last_payment_info) as TextView
        then:
            lastPaymentInfo.getText() == RuntimeEnvironment.application.getResources().getString(R.string.no_data_found)

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
            def dateFormatter = DateTimeFormat.forPattern(RuntimeEnvironment.application.getResources().getString(R.string.date_time_format)).withLocale(Locale.getDefault());
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

    def 'should correctly display number of trainings paid for and update when added'() {
        given:
            def overviewActivity = controller.get()
        when:
            def numberRemaining = overviewActivity.findViewById(R.id.number_of_trainings_remaining).getText()
        then:
            numberRemaining == 0 as String
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
        then:
            overviewActivity.findViewById(R.id.number_of_trainings_remaining).getText() == 8 as String
    }

    def 'should update number of trainings paid for correctly even when adding another payment on same day'() {
        given:
            def overviewActivity = controller.get()
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            def dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
        when:
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            dialog = ShadowDialog.latestDialog
            dialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            def confirmDialog = ShadowAlertDialog.latestAlertDialog
            confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            overviewActivity.findViewById(R.id.number_of_trainings_remaining).getText() == 16 as String
    }

    def 'should add training info when asked'() {
        given:
            def overviewActivity = controller.get()
        when: 'open the floating action menu'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        and: 'add payment to accept new trainings'
                overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
                ShadowDialog.latestDialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
                overviewActivity.findViewById(R.id.fab_menu).performClick()
        then:
            overviewActivity.findViewById(R.id.fab_action_add_training) != null
        when: 'click the add training button'
            overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
            def dialog = ShadowAlertDialog.latestAlertDialog
        then:
            dialog != null
        when: 'confirm add '
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            trainingDAO.queryForAll().size == 1
    }

    def 'should react to add training click, but not add training if cancel was pressed'() {
        given:
            def overviewActivity = controller.get()
        when: 'open the floating action menu'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        then:
            overviewActivity.findViewById(R.id.fab_action_add_training) != null
        when: 'click the add training button'
            overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
            def dialog = ShadowAlertDialog.latestAlertDialog
        then:
            dialog != null
        when: 'cancel add '
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick()
        then:
            trainingDAO.queryForAll().size == 0
    }

    def 'should add training and update remaining training number'() {
            given:
                def overviewActivity = controller.get()
                PaymentDaoHelper.addPayment()
            when: 'open the floating action menu'
                overviewActivity.findViewById(R.id.fab_menu).performClick()
            then:
                overviewActivity.findViewById(R.id.fab_action_add_training) != null
            when: 'click the add training button'
                overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
                def dialog = ShadowAlertDialog.latestAlertDialog
            then:
                dialog != null
            when: 'cancel add '
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
            then:
                (overviewActivity.findViewById(R.id.number_of_trainings_remaining) as TextView).getText() == 7 as String
    }

    def 'should ask for confirmation when adding second training on the same date'() {
        given:
            def overviewActivity = controller.get()
            TrainingsDaoHelper.addTraining()
        when: 'open the floating action menu'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        and: 'add payment to accept new trainings'
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            ShadowDialog.latestDialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        then:
            overviewActivity.findViewById(R.id.fab_action_add_training) != null
        when: 'click the add training button'
            overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
            def dialog = ShadowAlertDialog.latestAlertDialog
        then:
            dialog != null
        when: 'confirm adding'
            ShadowAlertDialog.reset()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            ShadowAlertDialog.latestAlertDialog != null
        when: 'confirm adding'
            ShadowAlertDialog.latestAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            trainingDAO.queryForAll().size() == 2
    }

    def 'should update last training displayed in activity when trainings added'() {
        given:
            def overviewActivity = controller.get()
        when: 'text view info text when db empty'
            def tw = overviewActivity.findViewById(R.id.last_training_info_date) as TextView
        then:
            tw.getText() == RuntimeEnvironment.application.getResources().getString(R.string.no_data_found);
        when: 'open the floating action menu'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        then:
            overviewActivity.findViewById(R.id.fab_action_add_training) != null
        when: 'add some payment to make sure you can add training'
            overviewActivity.findViewById(R.id.fab_action_add_payment).performClick()
            ShadowDialog.latestDialog.findViewById(R.id.add_payment_dialog_button_ok).performClick()
        and: 'click the add training button'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
            overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
            def dialog = ShadowAlertDialog.latestAlertDialog
        then:
            dialog != null
        when: 'confirm add'
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
            def dateFormatter = DateTimeFormat.forPattern(RuntimeEnvironment.application.getResources().getString(R.string.date_time_format)).withLocale(Locale.getDefault());
        then:
            (overviewActivity.findViewById(R.id.last_training_info_date) as TextView).getText() == dateFormatter.print(new DateTime())

    }

    def 'should update last training in activity when another training added on same day'() {
        given:
            TrainingsDaoHelper.addTraining()
            PaymentDaoHelper.addPayment()
            def overviewActivity = controller.get()
            (overviewActivity as RepositoryCallback).onDatasetChanged()
        when: 'open the floating action menu'
            overviewActivity.findViewById(R.id.fab_menu).performClick()
        then:
            overviewActivity.findViewById(R.id.fab_action_add_training) != null
            (overviewActivity.findViewById(R.id.number_of_trainings_remaining) as TextView).getText() == 7 as String
        when: 'click the add training button'
            overviewActivity.findViewById(R.id.fab_action_add_training).performClick()
            def dialog = ShadowAlertDialog.latestAlertDialog
        then:
            dialog != null
        when: 'confirm add'
            ShadowAlertDialog.reset()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            ShadowAlertDialog.latestAlertDialog != null
        when: 'confirm adding'
            ShadowAlertDialog.latestAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        then:
            (overviewActivity.findViewById(R.id.number_of_trainings_remaining) as TextView).getText() == 6 as String
    }

    def 'should open TrainingsActivity when clicked on trainings'() {
        given:
            def overviewActivity = controller.get()
            def nextActivityIntent = Shadows.shadowOf(overviewActivity as Activity)
        when:
            overviewActivity.findViewById(R.id.last_training_info_date).performClick()
        then:
            nextActivityIntent.peekNextStartedActivity().component.className == TrainingsActivity.class.name
        
    }
}
