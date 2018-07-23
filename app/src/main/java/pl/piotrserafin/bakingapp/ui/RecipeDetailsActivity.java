package pl.piotrserafin.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(getString(R.string.recipe))) {
            recipe = bundle.getParcelable(getString(R.string.recipe));
        }
    }
}
