package pl.piotrserafin.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Step;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsAdapterViewHolder> {

    private final Context context;
    final private RecipeDetailsAdapterOnClickHandler clickHandler;
    private ArrayList<Step> steps;

    public interface RecipeDetailsAdapterOnClickHandler {
        void onClick(int stepPosition);
    }

    public RecipeDetailsAdapter(Context context, RecipeDetailsAdapterOnClickHandler clickHandler,
                                ArrayList<Step> steps) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.step_row_item, parent, false);
        return new RecipeDetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailsAdapterViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.size();
    }

    public void setSteps(List<Step> steps) {
        this.steps.clear();
        this.steps.addAll(steps);
        notifyDataSetChanged();
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public class RecipeDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_short_description)
        TextView stepShortDescription;

        public RecipeDetailsAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickHandler.onClick(position);
        }
    }
}