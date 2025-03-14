package com.example.sportify;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * This SetUp method is used for logging the user in with a test account
     * to allow the test to continue running for this activity
     * This method will run first whenever you click on any method to test
     */
    @Before
    public void SetUp() {
        // Launch LoginActivity
        try {
            ActivityScenario<LoginActivity> loginActivityScenario = ActivityScenario.launch(LoginActivity.class);

            // Enter valid email and password
            Espresso.onView(withId(R.id.loginEmail))
                    .perform(ViewActions.typeText("johndoe@gmail.com"));
            Espresso.onView(withId(R.id.loginPassword))
                    .perform(ViewActions.typeText("12345"));

            // Click on the login button
            Espresso.onView(allOf(withId(R.id.btnLogin), withText("Login"))).perform(click());
        }
        catch (Exception e) {

        }
    }

    /**
     * This test is to try to update the name of the user
     */
    @Test
    public void testActivity_UpdateUserProfileFragment()
    {
        Espresso.onView(withId(R.id.user)).perform(click());

        Espresso.onView(withId(R.id.btnUpdate)).perform(click());

        Espresso.onView(withId(R.id.editName)).perform(ViewActions.replaceText("John Wick"));

        Espresso.onView(withId(R.id.btnUpdate)).perform(click());

    }

    /**
     * This test is to change to another activity (RunActivity)
     * by clicking on the "Run" item in the bottom navigation bar
     */
    @Test
    public void testActivityChangedFromBottomNavigation_ToRunActivity()
    {
        Intents.init();
        Espresso.onView(withId(R.id.map)).perform(click());

        // Check if the activity changes to RunActivity
        intended(hasComponent(RunActivity.class.getName()));
        Intents.release();
    }

    /**
     * This test is to change to another fragment, which is the userProfileFragment
     * by clicking the "User" item in the bottom navigation
     */
    @Test
    public void testFragmentChangedFromBottomNavigation_ToProfileFragment()
    {
        Espresso.onView(withId(R.id.user)).perform(click());
    }
}
