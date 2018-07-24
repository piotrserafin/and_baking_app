package pl.piotrserafin.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Ingredient;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.model.Step;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    final private RecipeDetailsAdapterOnClickHandler clickHandler;
    private Recipe recipe;

    public interface RecipeDetailsAdapterOnClickHandler {
        void onClick(Step step);
    }

    public RecipeDetailsAdapter(Context context, RecipeDetailsAdapterOnClickHandler clickHandler,
                          Recipe recipe) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { // Ingredients
            return new IngredientsAdapterViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_row_item, parent, false));
        } else { // Steps
            return new StepsAdapterViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_row_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientsAdapterViewHolder) {
            IngredientsAdapterViewHolder ingredientsAdapterViewHolder = (IngredientsAdapterViewHolder) holder;

            StringBuilder ingValue = new StringBuilder();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Ingredient ingredient = recipe.getIngredients().get(i);
                ingValue.append(String.format(Locale.getDefault(), "• %s (%.2f %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
                if (i != recipe.getIngredients().size() - 1)
                    ingValue.append("\n");
            }

            ingredientsAdapterViewHolder.ingredientsList.setText(ingValue.toString());

        } else if(holder instanceof StepsAdapterViewHolder) {
            StepsAdapterViewHolder stepsAdapterViewHolder = (StepsAdapterViewHolder) holder;

            Step step = recipe.getSteps().get(position);
            stepsAdapterViewHolder.stepShortDescription.setText(step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return (recipe.getSteps() == null) ? 0 : recipe.getSteps().size(); //Ingredients
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_short_description)
        TextView stepShortDescription;

        public StepsAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Step step = recipe.getSteps().get(position);
            clickHandler.onClick(step);
        }
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredients_text)
        TextView ingredientsList;

        public IngredientsAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
