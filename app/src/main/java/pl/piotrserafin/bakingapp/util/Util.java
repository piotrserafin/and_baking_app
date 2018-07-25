package pl.piotrserafin.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Recipe;
import timber.log.Timber;

public class Util {
    public static final String PREFS_INGREDIENTS = "ingredients";

    public static void storeRecipeToPrefs(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_INGREDIENTS, Context.MODE_PRIVATE).edit();
        prefs.putString(context.getString(R.string.pref_ingredients_key), serialize(recipe));
        prefs.apply();
    }

    public static Recipe loadRecipeFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_INGREDIENTS, Context.MODE_PRIVATE);
        String recipeBase64 = prefs.getString(context.getString(R.string.pref_ingredients_key), "");

        return "".equals(recipeBase64) ? null : deserialize(prefs.getString(context.getString(R.string.pref_ingredients_key), ""));
    }

    private static String serialize(Recipe recipe) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(recipe);
        } catch (JsonProcessingException e) {
            Timber.e(e);
        }
        return null;
    }

    private static Recipe deserialize(String recipe) {
        if (!"".equals(recipe)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(recipe, Recipe.class);
            } catch (IOException e) {
                Timber.e(e);
            }
        }
        return null;
    }
}
