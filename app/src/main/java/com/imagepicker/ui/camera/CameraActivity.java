package com.imagepicker.ui.camera;

import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.imagepicker.R;

/**
 * author by Anuj Sharma on 9/27/2017.
 */

public class CameraActivity extends AppCompatActivity implements CameraPresenterView.CameraView{
    private SurfaceView surfaceView;
    private ImageView btnCapture;
    private CameraActivityPresenterImpl presenterImpl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        surfaceView = findViewById(R.id.surface_camera);
        btnCapture = findViewById(R.id.btn_capture);
        presenterImpl = new CameraActivityPresenterImpl(this,this);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    @Override
    public ImageView getCaptureBtn() {
        return btnCapture;
    }
}
