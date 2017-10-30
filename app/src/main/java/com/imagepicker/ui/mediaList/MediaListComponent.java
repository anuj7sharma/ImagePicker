package com.imagepicker.ui.mediaList;

import com.imagepicker.dagger.component.AppComponent;
import com.imagepicker.dagger.PerActivity;

import dagger.Component;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = MediaListModule.class)
public interface MediaListComponent {
    void inject(MediaListActivity mediaListActivity);
}
