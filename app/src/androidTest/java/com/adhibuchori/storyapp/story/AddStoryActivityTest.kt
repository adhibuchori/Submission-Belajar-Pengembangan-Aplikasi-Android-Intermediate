package com.adhibuchori.storyapp.story

import androidx.test.core.app.ActivityScenario
import com.adhibuchori.storyapp.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adhibuchori.storyapp.data.remote.utils.EspressoIdlingResource
import com.adhibuchori.storyapp.ui.auth.login.LoginActivity
import com.adhibuchori.storyapp.ui.auth.welcome.WelcomeActivity
import com.adhibuchori.storyapp.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AddStoryActivityTest {

    private val description = "dummy_image_description"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() = runTest {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        ActivityScenario.launch(WelcomeActivity::class.java)
        ActivityScenario.launch(LoginActivity::class.java)
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.fab_add_story)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_story)).perform((click()))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun AddStoryComponentShowCorrectly() {
        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_add)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_current_location)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_update_current_location)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadStorySuccess() {
        onView(withId(R.id.btn_camera)).perform((click()))
        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform((click()))
        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))

        onView(withId(R.id.storyDescriptionEditText)).perform(typeText(description))
        onView(withId(R.id.storyDescriptionEditText)).perform(closeSoftKeyboard())

        onView(withId(R.id.cb_update_current_location)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_update_current_location)).perform((click()))
        onView(withId(R.id.btn_add)).perform((click()))
        onView(withId(R.id.rv_user_story)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadStoryError() {
        onView(withId(R.id.btn_camera)).perform((click()))
        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform((click()))
        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_update_current_location)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_update_current_location)).perform((click()))
        onView(withId(R.id.btn_add)).perform((click()))

        AddStoryComponentShowCorrectly()
    }


}