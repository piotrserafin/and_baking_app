package pl.piotrserafin.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Ingredient;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.ui.adapter.RecipeDetailsAdapter;
import pl.piotrserafin.bakingapp.widget.IngredientsWidgetRemoteViewService;
import timber.log.Timber;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {

    @BindView(R.id.recipe_details_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.ingredients_list)
    TextView ingredientsList;

    @BindView(R.id.recipe_details_list)
    RecyclerView stepsRecyclerView;

    private Recipe recipe;
    private RecipeDetailsAdapter recipeDetailsAdapter;

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        twoPane = getResources().getBoolean(R.bool.twoPane);

        prepareRecipeRecyclerView();

        //If restore
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.recipe_key))) {
            recipe = savedInstanceState.getParcelable(getString(R.string.recipe_key));
            if(recipe != null) {
                recipeDetailsAdapter.setSteps(recipe.getSteps());
                populateIngredients();
            }

        } else {

            //Get bundle passed by intent
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey(getString(R.string.recipe_key))) {
                recipe = bundle.getParcelable(getString(R.string.recipe_key));
                if(recipe != null) {
                    recipeDetailsAdapter.setSteps(recipe.getSteps());
                    populateIngredients();
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && recipe != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void prepareRecipeRecyclerView() {
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeDetailsAdapter = new RecipeDetailsAdapter(this, this, new ArrayList<>());
        stepsRecyclerView.setAdapter(recipeDetailsAdapter);
    }

    private void populateIngredients(){
        StringBuilder ingValue = new StringBuilder();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ingValue.append(String.format(Locale.getDefault(),
                    "• %s (%.2f %s)",
                    ingredient.getIngredient(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure()));

            if (i != recipe.getIngredients().size() - 1)
                ingValue.append("\n");
        }
        ingredientsList.setText(ingValue.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipe != null) {
            outState.putParcelable(getString(R.string.recipe_key), recipe);
        }
    }

    @Override
    public void onClick(int position) {
        if (twoPane) {
            Timber.d("OnClick(): TwoPaneMode");
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.step_key), recipe.getSteps().get(position));
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_list_container, fragment)
                    .commit();
        } else {
            Timber.d("OnClick(): SinglePaneMode");
            Intent stepDetailsIntent =
                    new Intent(RecipeDetailsActivity.this, StepDetailsActivity.class);
            stepDetailsIntent.putExtra(getString(R.string.steps_key), recipe.getSteps());
            stepDetailsIntent.putExtra(getString(R.string.step_position_key), position);
            startActivity(stepDetailsIntent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {
            IngredientsWidgetRemoteViewService.updateWidget(this, recipe);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
