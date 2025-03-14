package com.example.sportify;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * This test is to check if the user can login correctly with the correct email and password
     * If the email and password is correct, the activity will be changed to the MainActivity
     */
    @Test
    public void testLoginSuccess()
    {
        // Enter valid email and password
        Espresso.onView(withId(R.id.loginEmail))
                .perform(ViewActions.typeText("johndoe@gmail.com"));
        Espresso.onView(withId(R.id.loginPassword))
                .perform(ViewActions.typeText("12345"));
        // Initialise the Intents
        Intents.init();
        // Click on the login button
        Espresso.onView(withId(R.id.btnLogin)).perform(click());

        // Check if the activity changes to MainActivity
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();

    }

    /**
     * This test case is to test if the user can login with incorrect email and password
     * An Toast message will be displayed to the user if they have either/both credentials wrong
     */
    @Test
    public void testLoginFailure()
    {
        // Enter invalid email and password
        Espresso.onView(withId(R.id.loginEmail))
                .perform(ViewActions.typeText("notjohndoe@gmail.com"));
        Espresso.onView(withId(R.id.loginPassword))
                .perform(ViewActions.typeText("1234567890"));

        // Click on the login button
        Espresso.onView(withId(R.id.btnLogin)).perform(click());

    }

    /**
     * This test case is to check if the user is able to navigate to the register fragment
     * by clicking on the register button
     */
    @Test
    public void testGoToRegisterSuccess()
    {
        // Click on the register button
        Espresso.onView(withId(R.id.btnRegi)).perform(click());
        // Check if the component has been displayed - Fragment Checking
        Espresso.onView(withId(R.id.regiName)).check(matches(isDisplayed()));
    }

    /**
     * This test case is to check if the user is able to register an account with all the
     * input fields filled up, and also, with proper email address, correct password for both fields
     */
    @Test
    public void testRegistrationSuccess()
    {
        Espresso.onView(withId(R.id.btnRegi)).perform(click());
        // Enter all of the fields
        Espresso.onView(withId(R.id.regiName))
                .perform(ViewActions.replaceText("John Wick"));
        Espresso.onView(withId(R.id.regiEmail))
                .perform(ViewActions.replaceText("jw@gmail.com"));
        Espresso.onView(withId(R.id.regiPassword))
                .perform(ViewActions.replaceText("12345"));
        Espresso.onView(withId(R.id.regiRePassword))
                .perform(ViewActions.replaceText("12345"));
        Espresso.onView(withId(R.id.regiAge))
                .perform(ViewActions.replaceText("18"));
        Espresso.onView(withId(R.id.regiGenderText)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Male"))).perform(click());

        Espresso.onView(withId(R.id.btnRegister)).perform(click());
    }

    /**
     * This test case is to test if the user is able to register without entering any input fields
     * This will cause a Toast message to be displayed to the user to fill up the fields
     */
    @Test
    public void testRegistrationFailure_EmptyFields()
    {
        Espresso.onView(withId(R.id.btnRegi)).perform(click());
        Espresso.onView(withId(R.id.btnRegister)).perform(click());
    }

    /**
     * This test case is to check if the user enters the confirmation password differently from
     * the initial password or vice-versa, a Toast message will be displayed to inform the user
     */
    @Test
    public void testRegistrationFailure_PasswordMismatched()
    {
        Espresso.onView(withId(R.id.btnRegi)).perform(click());
        // Enter all of the fields
        Espresso.onView(withId(R.id.regiName))
                .perform(ViewActions.replaceText("John Wick"));
        Espresso.onView(withId(R.id.regiEmail))
                .perform(ViewActions.replaceText("jw@gmail.com"));
        Espresso.onView(withId(R.id.regiPassword))
                .perform(ViewActions.replaceText("12345"));
        Espresso.onView(withId(R.id.regiRePassword))
                .perform(ViewActions.replaceText("123"));
        Espresso.onView(withId(R.id.regiAge))
                .perform(ViewActions.replaceText("18"));
        Espresso.onView(withId(R.id.regiGenderText)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Male"))).perform(click());

        Espresso.onView(withId(R.id.btnRegister)).perform(click());
    }

    /**
     * This test case is to check if the user clicks on the "Back To Login" button
     * which will bring them back to the login page
     */
    @Test
    public void testRegistration_toLogin()
    {
        Espresso.onView(withId(R.id.btnRegi)).perform(click());

        Espresso.onView(allOf(withId(R.id.btnLogin), withText("Back To Login"))).perform(click());
    }
}
