package com.szymkowski.personaltrainercompanion.trainings
import android.app.Activity
import android.os.Build
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.szymkowski.personaltrainercompanion.BuildConfig
import com.szymkowski.personaltrainercompanion.R
import com.szymkowski.personaltrainercompanion.core.Database
import com.szymkowski.personaltrainercompanion.core.DateFormatter
import com.szymkowski.personaltrainercompanion.trainings.domain.Training
import org.joda.time.DateTime
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.util.ActivityController
import org.robospock.GradleRoboSpecification
import spock.lang.Shared
import spock.lang.Subject

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
class TrainingsActivityTest extends GradleRoboSpecification {

    @Subject
    TrainingsActivity trainingsActivity;

    ActivityController controller

    @Shared
    Dao<Training, Long> trainingsDao;

    def setupSpec() {
        trainingsDao = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), Database.class).getDao(Training.class)
    }

    def cleanupSpec() {
        OpenHelperManager.releaseHelper()
    }

    def setup() {
        controller = Robolectric.buildActivity(TrainingsActivity.class).create().start().resume().visible()
        cleanDb
    }

    def cleanup() {
        cleanDb
    }


    def 'should show list with recent training dates'() {
        given:
            def sample = obtain_sample 3 from_timespan_days 5
            ensure trainingsDao has sample
        and:
            def expected = create_expected_list sample for_pattern R.string.date_time_format
        when:
            def activity = controller.get()
        then:
            confirm activity shows expected in_recycler R.id.trainings_overview text_box R.id.training_date
    }

    def create_expected_list (List from) {
        [for_pattern: {@StringRes int pattern ->
            String format = RuntimeEnvironment.application.getApplicationContext().resources.getString(pattern)
            def formatter = DateFormatter.getDateFormatter(format)
            def result = []
            from.each {
                result << formatter.print(it.date)
            }
            result
        }
        ]
    }

    def confirm(Activity activity) {
        [shows: { strings ->
            [in_recycler: {int id ->
                        [text_box: { int id_field ->
                            RecyclerView view = activity.findViewById(id) as RecyclerView
                            def count = view.adapter.itemCount
                            view.scrollToPosition(count)
                            0.upto(count-1) {
                                def child = view.layoutManager.findViewByPosition(it)
                                def textView = child.findViewById(id_field) as TextView
                                assert (strings.contains(textView.text))
                                (strings as List).remove(textView.text)
                            }
                            assert ((strings as List).isEmpty())
                            return true
                            }
                        ]
                }
            ]}
        ]
    }

    def obtain_sample(int of) {
        [from_timespan_days: { days ->
            List result = []
            def rand = new Random()
            of.times {
                def training = new Training(DateTime.now().minusDays(rand.nextInt(days)))
                result << training
            }
            return result
        }
        ]


    }

    def ensure(Dao dao) {
        [has: {trainings ->
                for (Training t : trainings) {
                    dao.create(t)
                }
        }]
    }


    def getCleanDb() {
        trainingsDao.delete(trainingsDao.queryForAll())
    }

}
