package pl.piotrserafin.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.util.Util;

class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Recipe recipe;

    public IngredientsListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = Util.loadRecipeFromPrefs(this.context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list_item);

        row.setTextViewText(R.id.ingredient_item, recipe.getIngredients().get(position).getIngredient());

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
