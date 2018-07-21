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

import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private final Context context;
    final private RecipesAdapterOnClickHandler clickHandler;
    private ArrayList<Recipe> recipeList;

    public interface RecipesAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipesAdapter(Context context, RecipesAdapterOnClickHandler clickHandler,
                          ArrayList<Recipe> recipes) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.recipeList = recipes;
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_row_item, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return (recipeList == null) ? 0 : recipeList.size();
    }

    public void setRecipesList(List<Recipe> recipes) {
        recipeList.clear();
        recipeList.addAll(recipes);
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipes() {
        return recipeList;
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView recipeName;

        public RecipesAdapterViewHolder(View view) {
            super(view);

            recipeName = view.findViewById(R.id.recipe_name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe recipe = recipeList.get(position);
            clickHandler.onClick(recipe);
        }
    }
}
