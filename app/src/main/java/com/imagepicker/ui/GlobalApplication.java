package com.imagepicker.ui;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * author by Anuj Sharma on 9/27/2017.
 */

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Fresco.initialize(this);
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

    }
}
