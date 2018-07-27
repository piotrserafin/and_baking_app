package pl.piotrserafin.bakingapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Step;

public class StepDetailsActivity extends AppCompatActivity {

    private ArrayList<Step> steps;
    private int position = 0;

    @BindView(R.id.step_details_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_left)
    FloatingActionButton fabLeft;

    @BindView(R.id.fab_right)
    FloatingActionButton fabRight;

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

        //If restore
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.steps_key))) {
            steps = savedInstanceState.getParcelable(getString(R.string.steps_key));
            position = savedInstanceState.getInt(getString(R.string.step_position_key));

        } else {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey(getString(R.string.steps_key))
                    && bundle.containsKey(getString(R.string.step_position_key))) {
                steps = bundle.getParcelableArrayList(getString(R.string.steps_key));
                position = bundle.getInt(getString(R.string.step_position_key));
            }

            if(steps != null  && savedInstanceState == null) {
                Bundle stepBundle = new Bundle();
                stepBundle.putParcelable(getString(R.string.step_key), steps.get(position));
                StepDetailsFragment fragment = new StepDetailsFragment();
                fragment.setArguments(stepBundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_details_container, fragment)
                        .commit();
            }
        }

        if(steps != null) {
            prepareFabs();
        }
    }

    private void prepareFabs() {
        fabLeft.setOnClickListener(view -> {
            position--;
            stepTransition(steps.get(calculatePosition()));
        });

        fabRight.setOnClickListener(view -> {
            position++;
            stepTransition(steps.get(calculatePosition()));
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (steps != null && !steps.isEmpty()) {
            outState.putParcelableArrayList(getString(R.string.steps_key), steps);
            outState.putInt(getString(R.string.step_position_key), position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int calculatePosition() {
        if(position >= steps.size()) {
            position = 0;
        } else if (position < 0) {
            position = steps.size() - 1;
        }
        return position;
    }

    private void stepTransition(Step step) {
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(getString(R.string.step_key), step);
        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(stepBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_container, fragment)
                .commit();
    }
}
