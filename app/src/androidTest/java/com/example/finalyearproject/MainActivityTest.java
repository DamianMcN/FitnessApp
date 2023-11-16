package com.example.finalyearproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainMenu.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchOfMainMenuOnLoginButtonClick(){
        assertNotNull(mActivity.findViewById(R.id.btn_signin));

        onView(withId(R.id.btn_signin)).perform(click());

        Activity mainMenu = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(mainMenu);
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }
}