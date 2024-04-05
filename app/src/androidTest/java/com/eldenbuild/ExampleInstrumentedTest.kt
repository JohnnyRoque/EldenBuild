package com.eldenbuild

import android.app.Application
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnitRunner
import com.eldenbuild.application.TestEldenBuildApplication
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.ui.MainActivity
import com.eldenbuild.ui.builds_overview_fragment.OverviewRecyclerAdapter
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject


@RunWith(AndroidJUnit4::class)
@LargeTest
class EldenBuildAndroidUnitTest {


    @Before
    fun setUp() {
        val dummyRepo: BuildRepository by inject(
            BuildRepository::class.java,
        )

        runBlocking {
            dummyRepo.getAllBuildStream().collect {
                val roomIdlingResource = RoomIdlingResource("BuildDatabase", it)
                IdlingRegistry.getInstance().register(roomIdlingResource)
            }
        }
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testOpenBuild() {
        onView(withId(R.id.build_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<OverviewRecyclerAdapter.BuildViewHolder>(
                2,
                click()
            )
        )

    }
}

class RoomIdlingResource(
    private val databaseName: String,
    private val buildList: List<BuildCategories>

) : IdlingResource {
    private var isIdle: Boolean = false
    private var callback: ResourceCallback? = null
    override fun getName(): String {
        return databaseName
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback?) {
        this.callback = callback
    }

    override fun isIdleNow(): Boolean {
        isIdle = buildList.isNotEmpty()
        if (isIdle && callback != null) {
            callback?.onTransitionToIdle()
        }
        return isIdle
    }
}

class CustomInstrumentationTestRunner() : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestEldenBuildApplication::class.java.name, context)
    }

}

