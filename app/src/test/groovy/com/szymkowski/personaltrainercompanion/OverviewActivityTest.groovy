package com.szymkowski.personaltrainercompanion
import android.app.Activity
import android.os.Build
import android.widget.TextView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.szymkowski.personaltrainercompanion.payments.Database
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import pl.polidea.robospock.GradleRoboSpecification

@Config(constants = BuildConfig, sdk = Build.VERSION_CODES.KITKAT)
class OverviewActivityTest extends GradleRoboSpecification {

    def paymentDAO = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao()

    def 'should display no last payment information if database empty'() {
        given:
            paymentDAO.delete(paymentDAO.queryForAll())
            def overviewActivity = startActivity(OverviewActivity.class)
        when:
            TextView lastPaymentInfo =  overviewActivity.findViewById(R.id.last_payment_info)
        then:
            lastPaymentInfo.getText() == RuntimeEnvironment.application.getResources().getString(R.string.no_payment_found)
    }

    Activity startActivity(Class<? extends Activity> activityClass) {
        def controller = Robolectric.buildActivity(activityClass).create()
        controller.start()
        controller.resume()
        controller.visible()
        return controller.get()
    }
}
