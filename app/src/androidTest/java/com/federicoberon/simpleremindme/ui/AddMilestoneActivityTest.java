package com.federicoberon.simpleremindme.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.federicoberon.simpleremindme.MainActivity;
import com.federicoberon.simpleremindme.R;
import com.federicoberon.simpleremindme.TestDataHelper;
import com.federicoberon.simpleremindme.model.MilestoneXType;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddMilestoneActivityTest {

    private MilestoneXType milestone;
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public CountingTaskExecutorRule mCountingTaskExecutorRule = new CountingTaskExecutorRule();

    @Before
    public void setupMilestone(){
         milestone = TestDataHelper.MILESTONE_3;
    }

    @Test
    public void createNewMilestone(){

        // press add button
        onView(withId(R.id.fab))
                .perform(click());

        // set milestone title
        onView(withId(R.id.editTextMilestoneTitle))
                .perform(typeText(milestone.getTitle()));

        // set milestone type
        onView(withId(R.id.recyclerViewMilestoneType))
                .perform(RecyclerViewActions.actionOnItemAtPosition(milestone.getType()-1, click()));

        // set milestone description
        onView(withId(R.id.editTextDescriptionMilestone))
                .perform(typeText(milestone.getDesc()), closeSoftKeyboard());

        // set milestone date
        onView(withId(R.id.editTextDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(TestDataHelper.getDate1().get(Calendar.YEAR)
                        , TestDataHelper.getDate1().get(Calendar.MONTH)
                        , TestDataHelper.getDate1().get(Calendar.DAY_OF_MONTH)));
        onView(withId(android.R.id.button1)).perform(click());

        // set milestone time
        onView(withId(R.id.editTextTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(TestDataHelper.getDate1().get(Calendar.HOUR)
                        , TestDataHelper.getDate1().get(Calendar.MINUTE)));
        onView(withId(android.R.id.button1)).perform(click());

        // press OK button
        onView(withId(R.id.okButton))
                .perform(click());

        try {
            drain();
        } catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }

        // check that element are visible in a main list
        onView(withId(R.id.milestonesRecyclerView))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(milestone.getTitle()))));

    }

    @Test
    public void deleteMilestone(){
        // check that the milestone are there and click on it
        onView(withId(R.id.milestonesRecyclerView))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(milestone.getTitle()))));

        // click on it
        onView(withId(R.id.milestonesRecyclerView))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(milestone.getTitle())), click()));

        // click on delete button
        onView(withId(R.id.itemDelete))
                .perform(click());

        onView(withText(R.string.ok_button))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());

        // check that the milestone isn't there any more
        onView(hasDescendant(withText(milestone.getTitle()))).check(doesNotExist());
    }

    private void drain() throws TimeoutException, InterruptedException {
        mCountingTaskExecutorRule.drainTasks(1, TimeUnit.MINUTES);
    }
}
