package com.imagepicker.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.model.MessageEvent;
import com.imagepicker.utils.PermissionsAndroid;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.view.OrientationEventListener.ORIENTATION_UNKNOWN;

/**
 * author by Anuj Sharma on 10/3/2017.
 */

public class CameraActivityPresenterImpl implements View.OnClickListener {
    private static final String TAG = "custom camera";
    private CameraPresenterView.CameraView cameraView;
    private CameraActivity cameraActivity;
    private Camera camera;
    //    private Camera mCamera;
//    private CameraPreview mPreview;
    private Preview mPreview;
    private boolean isFrontCameraOpened;

    public CameraActivityPresenterImpl(CameraActivity cameraActivity, CameraPresenterView.CameraView cameraView) {
        this.cameraActivity = cameraActivity;
        this.cameraView = cameraView;
        init();
    }

    protected void init() {
        if (PermissionsAndroid.getInstance().checkCameraPermission(cameraActivity)) {
            cameraView.getCaptureBtn().setOnClickListener(this);
            cameraView.getSwitchCameraBtn().setOnClickListener(this);
            cameraView.getRecentImgView().setOnClickListener(this);
            // Create an instance of Camera
//            mCamera = getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new Preview(cameraActivity, cameraView.getSurfaceView());
            mPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            cameraView.getFrame().addView(mPreview);
            mPreview.setKeepScreenOn(true);

        } else {
            PermissionsAndroid.getInstance().requestForCameraPermission(cameraActivity);
        }
    }

    public void onResume() {
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                if (isFrontCameraOpened) {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    isFrontCameraOpened = true;
                } else {
                    camera = Camera.open();
                    isFrontCameraOpened = false;
                }

                camera.startPreview();
                mPreview.setCamera(camera);
            } catch (RuntimeException ex) {
                Toast.makeText(cameraActivity, "Camera Not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onPause() {
        if (camera != null) {
            camera.stopPreview();
            mPreview.setCamera(null);
            camera.release();
            camera = null;
        }
    }

    private void resetCam() {
        camera.startPreview();
        mPreview.setCamera(camera);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_capture:
                onOrientationChanged(270);
                camera.getParameters().setJpegQuality(90);
                camera.getParameters().setJpegThumbnailQuality(90);
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                break;
            case R.id.recent_clicked_img:
                //Move to previious screen with current camera gallery
                MessageEvent obj = new MessageEvent();
                obj.setCameraEventClicked(true);
                EventBus.getDefault().postSticky(obj);
                cameraActivity.finish();
                break;
            case R.id.btn_switch_camera:
                /*camera.stopPreview();
                mPreview.setCamera(null);
                if (isFrontCameraOpened) {
                    camera = Camera.open();
                    isFrontCameraOpened = false;
                } else {
                    System.out.println("front camera-> " + Camera.CameraInfo.CAMERA_FACING_FRONT);

                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    isFrontCameraOpened = true;
                }
                mPreview.setCamera(camera);
                camera.startPreview();*/
                break;
        }
    }

    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) return;
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(0, info);
        orientation = (orientation + 45) / 90 * 90;
        int rotation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else {  // back-facing camera
            rotation = (info.orientation + orientation) % 360;
        }
        camera.getParameters().setRotation(rotation);
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    public Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
        File outFile;

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;
            // Write to SD Card
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator +
                        cameraActivity.getString(R.string.app_name));
                if (!dir.isDirectory()) dir.mkdir();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                outFile = new File(dir, fileName);
                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
                refreshGallery(outFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //show recent clicked image on view left side
            if (outFile != null) {
                Uri uri = Uri.fromFile(outFile);
                cameraView.getRecentImgView().setImageURI(uri);
            }
        }
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        cameraActivity.sendBroadcast(mediaScanIntent);
    }

    /*private void maintainOrientation(String photoPath, Bitmap bitmap) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
//            Uri uri = Uri.fromFile(new File(photoPath));
            cameraView.getRecentImgView().setImageBitmap(rotatedBitmap);
//            cameraView.getRecentImgView().setImageURI(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }*/

}
