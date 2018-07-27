package pl.piotrserafin.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;
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

        String recipeImage = recipeList.get(position).getImage();
        if (!recipeImage.isEmpty()) {
            Picasso.get()
                    .load(recipeImage)
                    .into(holder.recipeImage);
        }
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

        @BindView(R.id.recipe_name)
        TextView recipeName;

        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        public RecipesAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

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
