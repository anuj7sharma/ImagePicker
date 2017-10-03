package com.imagepicker.ui.mediaList;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * author by Anuj Sharma on 10/3/2017.
 */
@Module
public class MediaListModule {
    private final MediaListView view;
    private final MediaListActivity mediaListActivity;

    public MediaListModule(MediaListActivity mediaListActivity, MediaListView view) {
        this.mediaListActivity = mediaListActivity;
        this.view = view;
    }

    @Provides
    //provides view to impl class
    public MediaListView provideView() {
        return view;
    }

    @Provides
    public Activity provideActivity() {
        return mediaListActivity;
    }

    @Provides
    public Context provideContext() {
        return mediaListActivity;
    }
}
