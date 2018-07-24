package pl.piotrserafin.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Step;
import pl.piotrserafin.bakingapp.ui.adapter.RecipeDetailsAdapter;

public class StepDetailsActivity extends AppCompatActivity {

    private Step step;

    @BindView(R.id.step_details_activity_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(getString(R.string.step))) {
            step = bundle.getParcelable(getString(R.string.step));
        }

        if (savedInstanceState == null) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(getString(R.string.step),step);
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(stepBundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_details_container, fragment)
                    .commit();
        }
    }
}
