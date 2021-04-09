package com.kahin.espressodemo

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.DriverAtoms.*
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kahin.espressodemo.ui.main.activity.WebViewActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebViewEspressoTest {
    @get:Rule
    val callActivityRule = ActivityScenarioRule(WebViewActivity::class.java)

    @Test
    fun typeTextInInput_clickButton_SubmitsForm() {
        onWebView().forceJavascriptEnabled()

        // Selects the WebView in your layout. If you have multiple WebViews you can also use a
        // matcher to select a given WebView, onWebView(withId(R.id.web_view)).
        onWebView()
                // Find the input element by ID
                .withElement(findElement(Locator.ID, "sb_form_q"))
                // Clear previous input
                .perform(clearElement())
                // Enter text into the input element
                .perform(DriverAtoms.webKeys("hello"))
                // Find the submit button
                .withElement(findElement(Locator.ID, "sb_form_go"))
                // Simulate a click via javascript
                .perform(webClick())
    }
}