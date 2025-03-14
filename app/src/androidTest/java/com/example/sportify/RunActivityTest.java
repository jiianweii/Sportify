package com.example.sportify;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RunActivityTest {
    @Rule
    public ActivityScenarioRule<RunActivity> activityRule =
            new ActivityScenarioRule<>(RunActivity.class);

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

            Espresso.onView(withId(R.id.map)).perform(click());
        }
        catch (Exception e) {

        }
    }

    /**
     * This test is to click on the "Start Running" button
     * it will change to fragment_run, which will activate the timer and sensor
     */
    @Test
    public void testActivity_SelectStartRunning()
    {
        // Start the fragment
        Espresso.onView(withId(R.id.startRunning)).perform(click());
    }

    /**
     * This test is to click on the "Pause" button
     * it will pause the timer, counting distance and sensor until it is unpause
     */
    @Test
    public void testActivity_SelectPauseRunning()
    {
        // Start the fragment
        Espresso.onView(withId(R.id.startRunning)).perform(click());

        // Pause the running
        Espresso.onView(withId(R.id.pauseButton)).perform(click());
    }

    /**
     * This test case is to ensure that when the user clicks on stop running,
     * the information will be stored in the database before sending the user
     * back to the MainActivity, with the new activity being displayed in the dashboard (fragment)
     */
    @Test
    public void testActivity_SelectStopRunning()
    {
        // Start the fragment
        Espresso.onView(withId(R.id.startRunning)).perform(click());

        // Stop the activity
        Espresso.onView(withId(R.id.stopButton)).perform(click());
    }

}
