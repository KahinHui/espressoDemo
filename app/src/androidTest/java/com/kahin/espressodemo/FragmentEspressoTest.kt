package com.kahin.espressodemo

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kahin.espressodemo.ui.main.fragment.FragmentActivity
import com.kahin.espressodemo.ui.main.fragment.MyListAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test

class FragmentEspressoTest {

    @get:Rule
    val fragmentActivityRule = ActivityScenarioRule(FragmentActivity::class.java)

    @Test
    fun itemWithText_click_checkResultText() {
        // Test the item which the "100" textView is paired with the "item 1" textView.
        onView(allOf(withText(COUNT), hasSibling(withText(ITEM_ONE_TITLE))))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withId(R.id.tv_result))
                .check(matches(withText(getResultText(ITEM_ONE_TITLE))))
    }

    @Test
    fun scrollToItem20_checkIsDisplayed() {
        // Test the recyclerView which id is rv.
        onView(withId(R.id.rv))
                .perform(
                        // Scrolls to the matched View, if it exists.
                        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                                hasDescendant(withText(ITEM_TWENTY_TITLE))
                        )
                )

        onView(withText(ITEM_TWENTY_TITLE))
                .check(matches(isDisplayed()))
    }

    @Test
    fun scrollToHolder_checkIsDisplayed() {
        onView(withId(R.id.rv))
                .perform(
                        // Scrolls to the matched View Holder, if it exists.
                        RecyclerViewActions.scrollToHolder(
                            isInTheEnd()
                        )
                )

        onView(withText(ITEM_FORTY_NINE_TITLE))
            .check(matches(isDisplayed()))
    }

    @Test
    fun scrollToPosition_checkIsDisplayed() {
        onView(withId(R.id.rv))
            .perform(
                // Scrolls to a specific position.
                RecyclerViewActions.scrollToPosition<MyListAdapter.ViewHolder>(
                    POSITION_MIDDLE
                )
            )

        onView(withText(ITEM_TWENTY_FIVE_TITLE))
            .check(matches(isDisplayed()))
    }

    @Test
    fun actionOnHolderItem_click_checkResultText() {
        onView(withId(R.id.rv))
            .perform(
                // Performs a View Action on a matched View Holder.
                RecyclerViewActions.actionOnHolderItem(
                    isInTheEnd(),
                    click()
                )
            )

        onView(withId(R.id.tv_result))
            .check(matches(withText(getResultText(ITEM_FORTY_NINE_TITLE))))
    }

    @Test
    fun actionOnItem_click_checkResultText() {
        onView(withId(R.id.rv))
            .perform(
                // Performs a View Action on a matched View.
                RecyclerViewActions.actionOnItem<MyListAdapter.ViewHolder>(
                    hasDescendant(withText(ITEM_TWENTY_TITLE)),
                    click()
                )
            )

        onView(withId(R.id.tv_result))
            .check(matches(withText(getResultText(ITEM_TWENTY_TITLE))))
    }

    @Test
    fun clickRecycleViewItem2_mainFragment() {
        onView(withId(R.id.rv))
                .perform(
                        // Performs a ViewAction on a view at a specific position.
                        RecyclerViewActions.actionOnItemAtPosition<MyListAdapter.ViewHolder>(
                            POSITION_START,
                                click()
                        )
                )

        onView(withId(R.id.tv_result))
                .check(matches(withText(getResultText(ITEM_ZERO_TITLE))))
    }

    private fun getResultText(result: String): String {
        return RESULT + result
    }

    /**
     * Matches the MyListAdapter.ViewHolder in the end of the list.
     */
    private fun isInTheEnd(): Matcher<MyListAdapter.ViewHolder> {
        return object : TypeSafeMatcher<MyListAdapter.ViewHolder>() {
            override fun matchesSafely(customHolder: MyListAdapter.ViewHolder): Boolean {
                return customHolder.getIsInTheEnd()
            }

            override fun describeTo(description: Description) {
                description.appendText(getResultText(ITEM_FORTY_NINE_TITLE))
            }
        }
    }


    companion object {
        const val RESULT = "Result: "
        const val COUNT = "100"

        const val ITEM_ZERO_TITLE = "item 0"
        const val ITEM_ONE_TITLE = "item 1"
        const val ITEM_TWENTY_TITLE = "item 20"
        const val ITEM_TWENTY_FIVE_TITLE = "item 25"
        const val ITEM_FORTY_NINE_TITLE = "item 49"

        const val POSITION_START = 0
        const val POSITION_MIDDLE = 25
    }
}