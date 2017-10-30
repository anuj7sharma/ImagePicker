package com.imagepicker.dagger.component;

import android.app.Activity;

import com.imagepicker.dagger.AScope;
import com.imagepicker.dagger.module.ActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author by Anuj Sharma on 10/30/2017.
 */
@AScope
@Singleton
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {
    Activity getActivity();
}
