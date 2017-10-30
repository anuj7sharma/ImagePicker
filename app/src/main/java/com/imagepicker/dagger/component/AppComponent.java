package com.imagepicker.dagger.component;

import com.imagepicker.dagger.module.ApplicationModule;
import com.imagepicker.ui.GlobalApplication;
import com.imagepicker.ui.PickerActivity;
import com.imagepicker.ui.cropper.CropperActivity;
import com.imagepicker.ui.mediaList.MediaListActivity;
import com.imagepicker.ui.selectedMedia.SelectedMediaActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {
    void inject(GlobalApplication globalApplication);

    void inject(PickerActivity pickerActivity);

    void inject(MediaListActivity mediaListActivity);

    void inject(SelectedMediaActivity selectedMediaActivity);

    void inject(CropperActivity cropperActivity);

//    SharedPreferencesHandler getSharedPrefrenceHandler();

}
