package pl.piotrserafin.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.util.Util;

public class IngredientsWidgetRemoteViewService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        Util.storeRecipeToPrefs(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new IngredientsListRemoteViewsFactory(getApplicationContext());
    }
}
