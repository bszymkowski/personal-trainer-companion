package com.szymkowski.personaltrainercompanion

import android.os.Build
import android.widget.TextView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.payments.Database
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification
import spock.lang.Shared

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class OverviewActivityTest extends GradleRoboSpecification {

    @Shared def paymentDAO;

    def setup() {
        paymentDAO.delete(paymentDAO.queryForAll())
    }

    def setupSpec() {
        paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao()
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }


    def 'should display no last payment information if database empty'() {
        given:
            def controller = Robolectric.buildActivity(OverviewActivity.class).create()
            controller.start()
            controller.resume()
            controller.visible()
        when:
            def overviewActivity = controller.get()
            TextView lastPaymentInfo =  overviewActivity.findViewById(R.id.last_payment_info)
        then:
            lastPaymentInfo.getText() == RuntimeEnvironment.application.getResources().getString(R.string.no_payment_found)
        cleanup:
            controller.pause()
            controller.destroy()
    }
}
