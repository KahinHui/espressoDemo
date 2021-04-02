package com.kahin.espressodemo

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class HomeEspressoTest {

    @get:Rule
    val homeActivityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun homeTest() {
        jumpMainActivity()

        Espresso.pressBack()

        jumpMainFragment()

        Espresso.pressBack()
    }

    @Test
    fun jumpMainActivity() {
        // Test the button which id is btn_activity.
        Espresso.onView(ViewMatchers.withId(R.id.btn_activity)).apply {
            // Check whether the button is displayed or not.
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            // Click the button.
            perform(ViewActions.click())
        }
    }

    @Test
    fun jumpMainFragment() {
        // Test the button which id is btn_fragment and content text is fragment.
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.btn_fragment), ViewMatchers.withText(R.string.fragment)))
                // Check whether the button is displayed or not.
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                // Click the button.
                .perform(ViewActions.click())
    }
}