package com.kahin.espressodemo

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.kahin.espressodemo.ui.main.activity.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class MainEspressoTest {

    @get:Rule
    val homeActivityRule = ActivityScenarioRule(MainActivity::class.java)

    private var isIntentInitialized = false

    @Before
    fun startUp() {
        Intents.init()
        isIntentInitialized = true

        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(ActivityResult(RESULT_OK, null))
    }

    @After
    fun finish() {
        if (isIntentInitialized) {
            // Otherwise will throw a NPE if Intents.init() wasn't called.
            Intents.release()
            isIntentInitialized = false
        }
    }

    @Test
    fun changeText_mainActivity() {

        onView(withId(R.id.et_name))
            .check(matches(withHint(R.string.name_hint)))
            // Type 'Tim' and close soft keyboard.
            .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard())
        onView(withId(R.id.btn_ok))
            .perform(click())
        onView(withId(R.id.tv_name))
            // Check whether content text is 'Name: Time' or not.
            .check(matches(withText("Name: $STRING_TO_BE_TYPED")))
    }

    @Test
    fun clickMenu_mainActivity() {
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

    @Test
    fun typePhone_ValidInput_InitiatesCall() {
        onView(withId(R.id.et_phone))
            .perform(typeText(STRING_PHONE), closeSoftKeyboard())
        onView(withId(R.id.btn_call))
            .perform(click())

        // Verify that an intent to the main was sent with the correct action, phone number
        // and package. Think of Intents intended API as the equivalent to Mockito's verify.
        intended(
            allOf(
                hasAction(Intent.ACTION_DIAL),
                hasData(INTENT_DATA_PHONE),
                toPackage(PACKAGE_DIALER)
            )
        )
    }

    @Test
    fun typePhone_ValidInput_InitiatesCall_truth() {
        onView(withId(R.id.et_phone))
            .perform(typeText(STRING_PHONE), closeSoftKeyboard())
        onView(withId(R.id.btn_call))
            .perform(click())

        // Intent validation that uses existing intent matchers that
        // matches an outgoing intent that call a phone
        val receivedIntent = Iterables.getOnlyElement(Intents.getIntents())
        assertThat(receivedIntent, hasAction(Intent.ACTION_DIAL))
        assertThat(receivedIntent, hasData(INTENT_DATA_PHONE))
    }

    @Test
    fun pickPhone_activityResult_DisplayPhone() {
        onView(withId(R.id.btn_pick_phone))
            .perform(click())

        onView(withId(R.id.btn_got))
            .perform(click())

        onView(withId(R.id.et_phone)).check(matches(withText(STRING_PICK_PHONE)))
    }

    @Test
    fun pickPhone_intending_activityResult_DisplayPhone() {
        // Stub all Intents to CallActivity to return STRING_PHONE.
        // Note that the Activity is never launched and result is stubbed.
        val resultData = Intent()
        resultData.putExtra(MainActivity.NAME_PHONE, STRING_PHONE)
        val result = ActivityResult(RESULT_OK, resultData)

        intending(hasComponent(hasShortClassName(".ui.main.activity.CallActivity")))
            .respondWith(result)

        onView(withId(R.id.btn_pick_phone))
            .perform(click())

        onView(withId(R.id.et_phone)).check(matches(withText(STRING_PHONE)))
    }

    @Test
    fun click_popupWindow() {
        onView(withId(R.id.btn_show_pop_up))
            .perform(click())

        onView(withText(R.string.espresso))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withText(R.string.espresso))
            .inRoot(RootMatchers.isSystemAlertWindow())
            .check(matches(isDisplayed()))
    }

    @Test
    fun click_dialog() {
        onView(withId(R.id.btn_show_dialog))
            .perform(click())

        onView(withText(R.string.yes))
            .inRoot(RootMatchers.isDialog())
            .perform(click())

        onView(withText(R.string.yes))
            .inRoot(RootMatchers.isSystemAlertWindow())
            .check(matches(isDisplayed()))
    }

    companion object {
        const val STRING_TO_BE_TYPED = "Tim"
        const val PACKAGE_DIALER = "com.google.android.dialer"
        const val STRING_PHONE = "138-8888-8888"
        const val STRING_PICK_PHONE = "987-654-321"

        val INTENT_DATA_PHONE: Uri = Uri.parse("tel:$STRING_PHONE")
    }
}