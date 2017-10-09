package com.imagepicker.ui.camera;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.utils.PermissionsAndroid;

/**
 * author by Anuj Sharma on 9/27/2017.
 */

public class CameraActivity extends AppCompatActivity implements CameraPresenterView.CameraView{
    private FrameLayout frameLayout;
    private ImageView btnCapture;
    private CameraActivityPresenterImpl presenterImpl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frameLayout = findViewById(R.id.frame_camera);
        btnCapture = findViewById(R.id.btn_capture);
        presenterImpl = new CameraActivityPresenterImpl(this,this);
    }

    @Override
    public FrameLayout getFrame() {
        return frameLayout;
    }

    @Override
    public ImageView getCaptureBtn() {
        return btnCapture;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsAndroid.CAMERA_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Show option to update profile
                    if (presenterImpl != null) {
                        presenterImpl.init();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Camera/Storage permission Denied", Toast.LENGTH_SHORT).show();
//                    Utils.getInstance().showToast("Camera/Storage permission Denied");
                }
            }

        }
    }
}
