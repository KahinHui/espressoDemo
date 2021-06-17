package com.kahin.espressodemo

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kahin.espressodemo.ui.main.fragment.FragmentActivity
import com.kahin.espressodemo.ui.main.fragment.MainFragment.Companion.ROW_TITLE
import com.kahin.espressodemo.ui.main.fragment.MyRecyclerViewAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
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

        onView(withId(R.id.tv_rv_result))
            .check(matches(withText(getRvResultText(ITEM_ONE_TITLE))))
    }

    @Test
    fun scrollToItem20_checkIsDisplayed() {
        // Test the recyclerView which id is rv.
        onView(withId(R.id.rv))
            .perform(
                // Scrolls to the matched View, if it exists.
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(ITEM_TWENTY_TITLE)),
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
                    withTitleContent(ITEM_TWENTY_NINE_TITLE)
                )
            )

        onView(withText(ITEM_TWENTY_NINE_TITLE))
            .check(matches(isDisplayed()))
    }

    @Test
    fun scrollToPosition_checkIsDisplayed() {
        onView(withId(R.id.rv))
            .perform(
                // Scrolls to a specific position.
                RecyclerViewActions.scrollToPosition<MyRecyclerViewAdapter.ViewHolder>(
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
                    withTitleContent(ITEM_TWENTY_NINE_TITLE),
                    itemViewClick(R.id.tv_title)
                )
            )

        onView(withId(R.id.tv_rv_result))
            .check(matches(withText(getRvResultText(COUNT))))
    }

    @Test
    fun actionOnItem_click_checkResultText() {
        onView(withId(R.id.rv))
            .perform(
                // Performs a View Action on a matched View.
                RecyclerViewActions.actionOnItem<MyRecyclerViewAdapter.ViewHolder>(
                    hasDescendant(withText(ITEM_TWENTY_TITLE)),
                    click()
                )
            )

        onView(withId(R.id.tv_rv_result))
            .check(matches(withText(getRvResultText(ITEM_TWENTY_TITLE))))
    }

    @Test
    fun clickRecycleViewItem0_mainFragment() {
        onView(withId(R.id.rv))
            .perform(
                // Performs a ViewAction on a view at a specific position.
                RecyclerViewActions.actionOnItemAtPosition<MyRecyclerViewAdapter.ViewHolder>(
                    POSITION_START,
                    itemViewClick(R.id.tv_title)
                )
            )

        onView(withId(R.id.tv_rv_result))
            .check(matches(withText(getRvResultText(COUNT))))
    }

    private fun getRvResultText(result: String): String {
        return RECYCLER_VIEW_RESULT + result
    }

    /**
     * Matches the MyListAdapter.ViewHolder with title content.
     */
    private fun withTitleContent(content: String): Matcher<MyRecyclerViewAdapter.ViewHolder> {
        return object : TypeSafeMatcher<MyRecyclerViewAdapter.ViewHolder>() {
            override fun matchesSafely(customHolder: MyRecyclerViewAdapter.ViewHolder): Boolean {
                val view = customHolder.itemView.findViewById<TextView>(R.id.tv_title)
                return view.text.equals(content)
            }

            override fun describeTo(description: Description) {
                description.appendText("ViewHolder with title content: $content")
            }
        }
    }

    /**
     * Click item view by Id.
     */
    private fun itemViewClick(@IdRes targetViewId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "ItemView is clicked."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v: View? = view?.findViewById(targetViewId)
                v?.performClick()
            }
        }
    }

    @Test
    fun clickListViewItem9_mainFragment() {
        // A click on the row with "item: 9".
        // Espresso scrolls through the list automatically as needed.
        onData(
            allOf(
                isA(MutableMap::class.java),
                hasEntry(equalTo(ROW_TITLE), `is`(ITEM_NINE_TITLE))
            )
        )
            .onChildView(withId(R.id.tv_title))
            .perform(click())

        onView(withId(R.id.tv_lv_result))
            .check(matches(withText(getLvResultText(ITEM_NINE_TITLE))))
    }

    @Test
    fun clickListViewItem1_mainFragment() {
        // A click on the row with "item: 1".
        // Espresso scrolls through the list automatically as needed.
        onData(
            allOf(
                isA(MutableMap::class.java),
                hasEntry(equalTo(ROW_TITLE), `is`(ITEM_NINE_TITLE))
            )
        )
            .onChildView(withId(R.id.tv_content))
            .perform(click())

        onView(withId(R.id.tv_lv_result))
            .check(matches(withText(getLvResultText(ITEM_NINE_COUNT))))
    }

    private fun getLvResultText(result: String): String {
        return LIST_VIEW_RESULT + result
    }

    companion object {
        const val RECYCLER_VIEW_RESULT = "RecyclerView Result: "
        const val LIST_VIEW_RESULT = "ListView Result: "
        const val COUNT = "100"

        const val ITEM_ONE_TITLE = "item 1"
        const val ITEM_NINE_TITLE = "item 9"
        const val ITEM_TWENTY_TITLE = "item 20"
        const val ITEM_TWENTY_FIVE_TITLE = "item 25"
        const val ITEM_TWENTY_NINE_TITLE = "item 29"

        const val POSITION_START = 0
        const val POSITION_MIDDLE = 25
        const val ITEM_NINE_COUNT = "9000"
    }
}