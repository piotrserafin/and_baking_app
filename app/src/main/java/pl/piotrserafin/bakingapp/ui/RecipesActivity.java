package pl.piotrserafin.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.BakingApp;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.api.RecipeApiClient;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.ui.adapter.RecipesAdapter;
import pl.piotrserafin.bakingapp.util.RecipesIdlingResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipesActivity extends AppCompatActivity
        implements RecipesAdapter.RecipesAdapterOnClickHandler {

    @BindView(R.id.recipes_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipes_activity_recycler_view)
    RecyclerView recipesRecyclerView;

    private RecipesAdapter recipesAdapter;
    private BakingApp application;

    // The Idling Resource which will be null in production.
    @Nullable
    private RecipesIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public RecipesIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipesIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getTitle());
        }

        prepareRecipesRecyclerView();

        getIdlingResource().setIdleState(false);

        if(savedInstanceState != null) {
            ArrayList<Recipe> recipes= savedInstanceState.getParcelableArrayList(getString(R.string.recipes_key));
            recipesAdapter.setRecipesList(recipes);
            updateLayout();
        } else {
            fetchRecipes();
        }
    }

    private void prepareRecipesRecyclerView() {

        recipesRecyclerView.setHasFixedSize(true);

        boolean twoPane = getResources().getBoolean(R.bool.twoPane);
        if (twoPane) {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        } else {
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        recipesAdapter = new RecipesAdapter(this, this, new ArrayList<>());
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Recipe> recipes = recipesAdapter.getRecipes();

        if (recipes != null && !recipes.isEmpty()) {
            outState.putParcelableArrayList(getString(R.string.recipes_key), recipes);
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Timber.d("Clicked recipe: %s", recipe.getName());

        Intent recipeDetailsIntent =
                new Intent(RecipesActivity.this, RecipeDetailsActivity.class);
        recipeDetailsIntent.putExtra(getString(R.string.recipe_key), recipe);
        startActivity(recipeDetailsIntent);
    }

    private void fetchRecipes() {

        Call<ArrayList<Recipe>> recipesCall = RecipeApiClient.getInstance().getRecipes();
        Callback<ArrayList<Recipe>> recipesCallback = new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call,
                                   Response<ArrayList<Recipe>> response) {
                if (!response.isSuccessful()) {
                    updateLayout();
                    return;
                }
                ArrayList<Recipe> recipes = response.body();

                if(recipes.isEmpty()) {
                    updateLayout();
                    return;
                }
                recipesAdapter.setRecipesList(recipes);

                getIdlingResource().setIdleState(true);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> moviesCall, Throwable t) {
                updateLayout();
            }
        };
        recipesCall.enqueue(recipesCallback);
    }

    private void updateLayout() {
        if (recipesAdapter.getItemCount() == 0) {
            findViewById(R.id.no_connection).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.no_connection).setVisibility(View.GONE);
        }
    }
}
