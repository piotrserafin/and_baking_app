package pl.piotrserafin.bakingapp;

import android.app.Application;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class BakingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }
    }
}
