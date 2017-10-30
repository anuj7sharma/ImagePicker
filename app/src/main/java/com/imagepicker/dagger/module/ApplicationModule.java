package com.imagepicker.dagger.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.imagepicker.R;
import com.imagepicker.dagger.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * author by Anuj Sharma on 10/30/2017.
 */

@Module
public class ApplicationModule {

    private final Application globalActivity;

    public ApplicationModule(Application app) {
        globalActivity = app;
    }

    @Provides
    Context provideApplicationContext() {
        return globalActivity;
    }

    @Provides
    @ApplicationContext
    Application provideApplication() {
        return globalActivity;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefrence() {
        return globalActivity.getSharedPreferences(globalActivity.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
