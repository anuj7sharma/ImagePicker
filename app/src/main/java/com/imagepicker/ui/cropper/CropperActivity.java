package com.imagepicker.ui.cropper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.cropper.CropImage;
import com.imagepicker.cropper.CropImageOptions;
import com.imagepicker.cropper.CropImageView;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.PermissionsAndroid;

import java.io.File;
import java.io.IOException;

/**
 * author by Anuj Sharma on 9/25/2017.
 */

public class CropperActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    private CropImageView cropImageView;
    private CropImageOptions cropImageOptions;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        menu.findItem(R.id.action_count).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(this)) {
                    cropImage();
                } else {
                    PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(this);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        cropImageView.setOnSetImageUriCompleteListener(null);
        cropImageView.setOnCropImageCompleteListener(null);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        initViews();
        if (getIntent() != null && getIntent().getParcelableExtra(Constants.SELECTED_MEDIA_LIST_OBJ) != null) {
            MediaItemBean mediaObj = getIntent().getParcelableExtra(Constants.SELECTED_MEDIA_LIST_OBJ);
            System.out.println("MediaSize-> " + mediaObj.getMediaSize());
            System.out.println("MediaHeight-> " + mediaObj.getHeight());
            System.out.println("MediaWidth-> " + mediaObj.getWidth());

            cropImageView.setImageUriAsync(Uri.fromFile(new File(mediaObj.getMediaPath())));
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_crop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cropImageView = findViewById(R.id.cropImageView);
        cropImageOptions = new CropImageOptions();

    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            if (cropImageOptions.initialCropWindowRectangle != null) {
                cropImageView.setCropRect(cropImageOptions.initialCropWindowRectangle);
            }
            if (cropImageOptions.initialRotation > -1) {
                cropImageView.setRotatedDegrees(cropImageOptions.initialRotation);
            }

        } else {
//            setResult(null, error, 1);
            Toast.makeText(this, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        setResult(result.getUri(), result.getError(), result.getSampleSize());
    }

    /**
     * Result with cropped image data or error if failed.
     */
    protected void setResult(Uri uri, Exception error, int sampleSize) {
        int resultCode = error == null ? RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
//        setResult(resultCode, getResultIntent(uri, error, sampleSize));
//        finish();

        Intent dataIntent = new Intent();
        dataIntent.setData(uri);
        setResult(resultCode, dataIntent);
        finish();
    }

    /**
     * Execute crop image and save the result tou output uri.
     */
    protected void cropImage() {
        Uri outputUri = getOutputUri();
        cropImageView.saveCroppedImageAsync(outputUri,
                cropImageOptions.outputCompressFormat,
                cropImageOptions.outputCompressQuality,
                cropImageOptions.outputRequestWidth,
                cropImageOptions.outputRequestHeight,
                cropImageOptions.outputRequestSizeOptions);
    }

    protected void rotateImage(int degrees) {
        cropImageView.rotateImage(degrees);
    }

    protected Uri getOutputUri() {
        Uri outputUri = cropImageOptions.outputUri;
        if (outputUri == null || outputUri.equals(Uri.EMPTY)) {
            try {
                String ext = cropImageOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" :
                        cropImageOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
                outputUri = Uri.fromFile(File.createTempFile("cropped", ext, getCacheDir()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp file for output image", e);
            }
        }
        return outputUri;
    }


}
