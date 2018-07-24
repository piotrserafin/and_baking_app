package pl.piotrserafin.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.model.Step;
import pl.piotrserafin.bakingapp.ui.adapter.RecipeDetailsAdapter;
import timber.log.Timber;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {

    @BindView(R.id.recipe_details_activity_toolbar)
    Toolbar toolbar;

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

        if (findViewById(R.id.recipe_details_list_container) != null) {
            twoPane = true;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(getString(R.string.recipe))) {
            recipe = bundle.getParcelable(getString(R.string.recipe));

            stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recipeDetailsAdapter = new RecipeDetailsAdapter(this, this, recipe);
            stepsRecyclerView.setAdapter(recipeDetailsAdapter);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(Step step) {
        if (twoPane) {
            Timber.d("OnClick(): TwoPaneMode");
//            Bundle bundle = new Bundle();
//            Fragment fragment = new Fragment();
//            fragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.recipe_details_list_container, fragment)
//                    .commit();
        } else {
            Timber.d("OnClick(): SinglePaneMode");
//            Intent recipeStepsIntent = new Intent(RecipeDetailsActivity.this, RecipeStepsActivity.class);
//            startActivity(recipeStepsIntent);
        }
    }
}
