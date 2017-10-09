package com.imagepicker.ui.camera;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;

import com.imagepicker.R;
import com.imagepicker.utils.PermissionsAndroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

public class CameraActivityPresenterImpl implements View.OnClickListener {
    private CameraPresenterView.CameraView cameraView;
    private CameraActivity cameraActivity;

    private Camera mCamera;
    private CameraPreview mPreview;

    public CameraActivityPresenterImpl(CameraActivity cameraActivity, CameraPresenterView.CameraView cameraView) {
        this.cameraActivity = cameraActivity;
        this.cameraView = cameraView;
        init();
    }

    protected void init() {
        if (PermissionsAndroid.getInstance().checkCameraPermission(cameraActivity)) {
            cameraView.getCaptureBtn().setOnClickListener(this);
            // Create an instance of Camera
            mCamera = getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(cameraActivity, mCamera);
            cameraView.getFrame().addView(mPreview);
        } else {
            PermissionsAndroid.getInstance().requestForCameraPermission(cameraActivity);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_capture:
//                mCamera.takePicture(null, null, mPicture);
                break;
        }
    }

    /*private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };*/
}
