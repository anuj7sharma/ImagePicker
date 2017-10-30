package com.imagepicker.dagger.module;

import android.app.Activity;
import android.content.Context;

import com.imagepicker.dagger.ActivityContext;
import com.imagepicker.dagger.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * author by Anuj Sharma on 10/30/2017.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

}
