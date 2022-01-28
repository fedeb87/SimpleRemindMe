package com.federicoberon.simpleremindme.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import android.view.Gravity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.federicoberon.simpleremindme.MainActivity;
import com.federicoberon.simpleremindme.R;
import com.federicoberon.simpleremindme.TestDataHelper;
import com.federicoberon.simpleremindme.ui.home.MilestoneAdapter;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Objects;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setupRecyclerView(){
        // Set dummy data to recyclerview adapter
        activityScenarioRule.getScenario().onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.milestonesRecyclerView);
            ((MilestoneAdapter) Objects.requireNonNull(recyclerView.getAdapter()))
                    .setMilestones(Arrays.asList(TestDataHelper.MILESTONE_1, TestDataHelper.MILESTONE_2));
        });
    }

    @Test
    public void clickOnFirstItem() {

        // When clicking on the first milestone
        onView(withId(R.id.milestonesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Then the second screen with the milestone details should appear.
        onView(withId(R.id.textViewTitle))
                .check(matches(isDisplayed()));

        onView(withId(R.id.textViewTitle)).check(matches(withText(TestDataHelper.MILESTONE_1.getTitle())));
    }

    @Test
    public void clickOnAboutMenu(){

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_about));


        onView(withId(R.id.textAbout)).check(matches(isDisplayed()));
    }

    @Test
    public void findElementTest(){
        onView(withId(R.id.edit_text_search))
                .perform(typeText("uncle Bill"));

        onView(withId(R.id.buttonSearch))
                .perform(click());

        String itemElementText = TestDataHelper.MILESTONE_1.getDesc();
        onView(withText(itemElementText)).check(matches(isDisplayed()));

        String itemElementText2 = TestDataHelper.MILESTONE_2.getDesc();

        onView(withText(itemElementText2)).check(doesNotExist());

    }

    @Test
    public void clickOnNewMilestone(){
        onView(withId(R.id.fab))
                .perform(click());

        onView(allOf(withId(R.id.textViewEventType),withText(R.string.event_type_title)))
                .check(matches(isDisplayed()));
    }
}