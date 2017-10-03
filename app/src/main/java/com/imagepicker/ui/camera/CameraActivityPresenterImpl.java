package com.imagepicker.ui.camera;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

public class CameraActivityPresenterImpl {
    private CameraPresenterView.CameraView cameraView;
    private CameraActivity cameraActivity;

    public CameraActivityPresenterImpl(CameraActivity cameraActivity, CameraPresenterView.CameraView cameraView) {
        this.cameraActivity = cameraActivity;
        this.cameraView = cameraView;
        if (checkCameraHardware(cameraActivity)) {
            init();
        }
    }

    private void init() {

    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
