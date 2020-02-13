package com.alexander.arenatest;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.alexander.arenatest.view.MainActivity;
import com.alexander.arenatest.view.PullRequestsActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRepository = new ActivityTestRule<>(
            MainActivity.class,
            true,
            true);

    @Test
    public void testListItemClick() {
        onView(isRoot()).perform(waitFor(10000));

        Intents.init();
        onView(ViewMatchers.withId(R.id.rv_repositories))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(activityRepository.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot()).perform(waitFor(5000));

        intended(hasComponent(PullRequestsActivity.class.getName()));
    }

    @Test
    public void testListScroll() {
        onView(isRoot()).perform(waitFor(10000));

        RecyclerView recyclerView = activityRepository.getActivity().findViewById(R.id.rv_repositories);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(ViewMatchers.withId(R.id.rv_repositories))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(activityRepository.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));

        onView(isRoot()).perform(waitFor(5000));

    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
