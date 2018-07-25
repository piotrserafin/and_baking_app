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

        fabLeft.setOnClickListener(view -> {
            position--;
            stepTransition(steps.get(calculatePosition()));
        });

        fabRight.setOnClickListener(view -> {
            position++;
            stepTransition(steps.get(calculatePosition()));
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(getString(R.string.steps))
                && bundle.containsKey(getString(R.string.step_position))) {
            steps = bundle.getParcelableArrayList(getString(R.string.steps));
            position = bundle.getInt(getString(R.string.step_position));
        }

        if (savedInstanceState == null) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(getString(R.string.step),steps.get(position));
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(stepBundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_details_container, fragment)
                    .commit();
        }
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
        stepBundle.putParcelable(getString(R.string.step), step);
        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(stepBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_container, fragment)
                .commit();
    }
}
