package pl.piotrserafin.bakingapp.api;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import pl.piotrserafin.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipeApiClient {

    private static final String TAG = RecipeApiClient.class.getSimpleName();

    private static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static RecipeApiClient instance = null;

    private RecipeApiClient.Api api;

    public interface Api {
        @GET("baking.json")
        Call<ArrayList<Recipe>> getRecipes();
    }

    private RecipeApiClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        api = retrofit.create(RecipeApiClient.Api.class);
    }

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public Call<ArrayList<Recipe>> getRecipes() {
        return api.getRecipes();
    }
}
