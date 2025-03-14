package com.example.sportify;

import android.content.Context;
import android.util.Log;

import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DatabaseHelperTest {
    private Context mContext;
    private DatabaseHelper databaseHelper;
    String sampleEmail = "johndoe@gmail.com";
    String samplePassword = "12345";
    long expectedResult = 0;

    /**
     * To set up the database before running any tests to prevent errors from happening
     */
    @Before
    public void SetUp()
    {
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseHelper = new DatabaseHelper(mContext);
    }

    /**
     * This test case is to check if the database has inserted the information correctly
     */
    @Test
    public void testInsertActivity() {
        long result = databaseHelper.insertActivity("27/03/2024", "00:05:13", "1.14", sampleEmail);
        // Result returns the row id, therefore it's definitely more than 0
        // If the result returns less than 0, it means that the activity is not inserted
        assertThat("Check if insertion is correct", result, greaterThan(expectedResult));
    }

    /**
     * This test case is to check if the activities are returned correctly
     * return true, IF the activities size is more than 0
     */
    @Test
    public void testGetActivityData() {
        // This arraylist will store all of the activities that belongs to one user
        ArrayList<Activity> activities = databaseHelper.getActivityData(sampleEmail);

        assertTrue("Check if database returns all of the activities", activities.size() > 0);
    }

    /**
     * This test is to check if the email belongs to a user
     * if the user exists, we will get the Account information
     * which will be used later in the application
     */
    @Test
    public void testGetUser() {
        Account account = databaseHelper.getUser(sampleEmail);
        assertEquals("Check if the account belongs to the same email", account.getEmail(), sampleEmail);
    }

    /**
     * This test is to check if the user data has been inserted correctly
     */
    @Test
    public void testInsertUserData() {
        boolean result = databaseHelper.insertUserData("Test Case", "testcase@gmail.com", "12345", "18", "Male");
        assertTrue("Check if user can register successfully", result);
    }

    /**
     * This test is to check if the user data can be updated
     */
    @Test
    public void testUpdateUserData() {
        String newPassword = "123456";

        // Update the database with the new password
        databaseHelper.updateUserData("", sampleEmail, newPassword, "", "");

        // Get the new password from the database
        Account user = databaseHelper.getUser(sampleEmail);

        // Check if the new password has been inserted correct
        assertThat("Check if new password is correctly inserted", user.getPassword().equals(newPassword));

    }

    /**
     * This test case is to check if the username has been used
     * return true, IF the username is in the database
     */
    @Test
    public void testCheckUsername() {
        boolean result = databaseHelper.checkUsername(sampleEmail);
        assertTrue("Check if email has been registered", result);
    }

    /**
     * This test case is to check for username and password verification
     * if both email and password is correct, it will return true
     */
    @Test
    public void testCheckUsernamePassword() {
        boolean result = databaseHelper.checkUsernamePassword(sampleEmail, samplePassword);
        assertTrue("Check if email and password is correct", result);
    }
}