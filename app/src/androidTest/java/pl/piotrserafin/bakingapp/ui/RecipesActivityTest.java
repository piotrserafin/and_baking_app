package pl.piotrserafin.bakingapp.ui;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.util.Util;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> activityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void clickOnFirstRecipe_opensRecipeDetailsActivity() {

        onView(withId(R.id.recipes_activity_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withId(R.id.ingredients_list))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_details_list))
                .check(matches(isDisplayed()));
    }


    @Test
    public void clickAddToWidgetButton_checkIfLoadedIntoWidgetViewAndStoredInSharedPref() {

        RecipesActivity activity = activityTestRule.getActivity();

        activity.getApplicationContext()
                .getSharedPreferences(activity.getString(R.string.pref_ingredients_key),
                        Context.MODE_PRIVATE).edit()
                .clear()
                .commit();

        onView(withId(R.id.recipes_activity_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.action_add_to_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        Recipe recipe = Util.loadRecipeFromPrefs(activity.getApplicationContext());

        assertNotNull(recipe);
        assertEquals("Yellow Cake", recipe.getName());
    }
}