package com.kahin.espressodemo

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainEspressoTest {

    @get:Rule
    val homeActivityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun mainTest() {
        changeText_mainActivity()

        pressBack()

        jumpMainFragment()

        pressBack()
    }

    @Test
    fun jumpMainActivity() {
        // test the button which id is btn_activity.
        onView(withId(R.id.btn_activity)).apply {
            // check whether the button is displayed or not.
            check(matches(isDisplayed()))
            // click the button.
            perform(click())
        }
    }

    @Test
    fun jumpMainFragment() {
        // test the button which id is btn_fragment and content text is fragment.
        onView(allOf(withId(R.id.btn_fragment), withText(R.string.fragment)))
                // check whether the button is displayed or not.
                .check(matches(isDisplayed()))
                // click the button.
                .perform(click())
    }


    @Test
    fun changeText_mainActivity() {
        jumpMainActivity()

        onView(withId(R.id.btn_activity))
                // check whether the button isn't exist or not.
                .check(doesNotExist())

        onView(withId(R.id.et_name))
                .check(matches(withHint(R.string.name_hint)))
                // type 'Tim' and close soft keyboard.
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard())
        onView(withId(R.id.btn_ok))
                .perform(click())
        onView(withId(R.id.tv_name))
                // check whether content text is 'Name: Time' or not.
                .check(matches(withText("Name: $STRING_TO_BE_TYPED")))
    }

    @Test
    fun clickMenu_mainActivity() {
        jumpMainActivity()

        onView(withId(R.id.btn_show_actionbar))
                .perform(click())

        // Open the options menu OR open the overflow menu, depending on whether
        // the device has a hardware or software overflow menu button.
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        // Click the item.
        onView(withText("Key"))
                .perform(click())

        onView(withId(R.id.btn_hide_actionbar))
                .perform(click())
    }

    companion object {
        const val STRING_TO_BE_TYPED = "Tim"
    }
}