package com.adhibuchori.storyapp.auth

import androidx.recyclerview.widget.RecyclerView
import com.adhibuchori.storyapp.R
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adhibuchori.storyapp.data.remote.utils.EspressoIdlingResource
import com.adhibuchori.storyapp.ui.auth.welcome.WelcomeActivity
import com.adhibuchori.storyapp.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WelcomeActivityTest {
    private val email = "bebekgoreng@gmail.com"
    private val password = "bebekgoreng"

    private val wrongEmail = "wrongemail@gmail.com"
    private val wrongPassword = "wrongpassword"

    private val invalidEmail = "invalid_email"
    private val invalidPassword = "invalid_password"

    @Before
    fun setUp() = runTest {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        ActivityScenario.launch(WelcomeActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun welcomeComponentShowCorrectly() {
        onView(withId(R.id.iv_welcome)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.descTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.signupButton)).check(matches(isDisplayed()))
        onView(withId(R.id.copyrightTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun moveToSignUpComponentShowCorrectly() {
        onView(withId(R.id.signupButton)).check(matches(isDisplayed()))
        onView(withId(R.id.signupButton)).perform(click())
        onView(withId(R.id.iv_signup)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.nameTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.nameEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.emailTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.signupButton)).check(matches(isDisplayed()))
        onView(withId(R.id.copyrightTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun moveToLoginComponentShowCorrectly() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.iv_login)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.emailTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditTextLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.copyrightTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun loginSuccess() {
        Intents.init()

        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.emailEditText)).perform(typeText(email))
        onView(withId(R.id.emailEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText(password))
        onView(withId(R.id.passwordEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.rv_user_story)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )

        Intents.intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun loginWrong() {
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.emailEditText)).perform(typeText(wrongEmail))
        onView(withId(R.id.emailEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText(wrongPassword))
        onView(withId(R.id.passwordEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        moveToLoginComponentShowCorrectly()
    }

    @Test
    fun loginInvalid() {
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.emailEditText)).perform(typeText(invalidEmail))
        onView(withId(R.id.emailEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText(invalidPassword))
        onView(withId(R.id.passwordEditText)).perform(closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        moveToLoginComponentShowCorrectly()
    }

    @Test
    fun logoutSuccess() = runTest {
        Intents.init()

        onView(withId(R.id.rv_user_story)).check(matches(isDisplayed()))
        onView(withId(R.id.action_logout)).perform(click())

        Intents.intended(hasComponent(WelcomeActivity::class.java.name))
    }
}