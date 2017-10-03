package com.imagepicker.ui.camera;

import android.view.SurfaceView;
import android.widget.ImageView;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

public interface CameraPresenterView {
    interface CameraPresenter {
        void onCaptureImage();
    }

    interface CameraView {
        SurfaceView getSurfaceView();
        ImageView getCaptureBtn();
    }
}
