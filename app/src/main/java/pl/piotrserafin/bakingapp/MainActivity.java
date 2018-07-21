package pl.piotrserafin.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import pl.piotrserafin.bakingapp.model.Recipe;
import pl.piotrserafin.bakingapp.api.RecipeApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fetchRecipes();
    }

    private void fetchRecipes() {

        Call<ArrayList<Recipe>> recipesCall = RecipeApiClient.getInstance().getRecipes();
        Callback<ArrayList<Recipe>> recipesCallback = new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call,
                                   Response<ArrayList<Recipe>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<Recipe> recipes = response.body();

                Log.d(TAG, recipes.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> moviesCall, Throwable t) {

            }
        };
        recipesCall.enqueue(recipesCallback);
    }
}
