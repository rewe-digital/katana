package org.rewedigital.katana.android.example.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.main.view.MainActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
/**
 * This Espresso test showcases how Katana modules can be overwritten/replaced for UI tests with
 * mock/test variants. Instead of an actual network operation, static mock data is returned which
 * makes the test stable and predictable.
 *
 * @see org.rewedigital.katana.android.example.remote.TestJsonPlaceholderRepository
 * @see org.rewedigital.katana.android.example.remote.JsonPlaceholderApiSuccessMock
 */
class MainEspressoTest {

    init {
        val repositoryIdlingResource = CountingIdlingResource("JsonPlaceholderRepository")

        // Overwrite modules with mock implementation
        Modules.modules = listOf(
            testSuccessApiMockModule,
            createTestRepositoryModule(repositoryIdlingResource)
        )

        IdlingRegistry.getInstance().register(repositoryIdlingResource)
    }

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRemote() {
        onView(withText("Remote")).perform(ViewActions.click())
        onView(withId(R.id.textView)).check(matches(withText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam")))
    }
}
