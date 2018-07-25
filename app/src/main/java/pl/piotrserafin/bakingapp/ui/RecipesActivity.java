package pl.piotrserafin.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.api.RecipeApiClient;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.ui.adapter.RecipesAdapter;
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

        recipesRecyclerView.setHasFixedSize(true);

        boolean twoPane = getResources().getBoolean(R.bool.twoPane);
        if (twoPane) {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        } else {
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        recipesAdapter = new RecipesAdapter(this, this, new ArrayList<>());
        recipesRecyclerView.setAdapter(recipesAdapter);

        fetchRecipes();
    }

    @Override
    public void onClick(Recipe recipe) {
        Timber.d("Clicked recipe: %s", recipe.getName());

        Intent recipeDetailsIntent =
                new Intent(RecipesActivity.this, RecipeDetailsActivity.class);
        recipeDetailsIntent.putExtra(getString(R.string.recipe), recipe);
        startActivity(recipeDetailsIntent);
    }

    private void fetchRecipes() {

        Call<ArrayList<Recipe>> recipesCall = RecipeApiClient.getInstance().getRecipes();
        Callback<ArrayList<Recipe>> recipesCallback = new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call,
                                   Response<ArrayList<Recipe>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<Recipe> recipes = response.body();

                if(recipes.isEmpty()) {
                    return;
                }
                recipesAdapter.setRecipesList(recipes);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> moviesCall, Throwable t) {

            }
        };
        recipesCall.enqueue(recipesCallback);
    }
}
