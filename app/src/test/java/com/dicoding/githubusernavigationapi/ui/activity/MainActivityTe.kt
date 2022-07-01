package com.dicoding.githubusernavigationapi.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.githubusernavigationapi.R
import com.dicoding.githubusernavigationapi.adapter.ListUserAdapter
import org.junit.Rule

import org.junit.Test

class MainActivityTe {

//    @get:Rule
//    var myActivityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun onOptionsItemSelected() {
        onView(ViewMatchers.withId(R.id.favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.favorite)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.rv_user_favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.rv_user_favorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
    }
}