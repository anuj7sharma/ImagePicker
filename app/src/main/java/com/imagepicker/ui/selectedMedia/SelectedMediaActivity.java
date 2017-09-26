package com.imagepicker.ui.selectedMedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.cropper.CropperActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.DetailViewPagerTransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaActivity extends AppCompatActivity implements SelectedMediaView {
    private SelectedMediaPresenterImpl presenterImpl;
    private HashMap<String, MediaItemBean> selectedMediaMap;
    private Toolbar toolbar;
    private ViewPager selectedViewPager;
    private RecyclerView selectedMediaRecycler;
    public static final int CROP_IMAGE_REQUEST_CODE = 501;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selected_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (presenterImpl != null) {
                    presenterImpl.deleteMedia();
                }
                break;
            case R.id.action_crop:
                //Move to Seperate Activity for Cropping
                if (SelectedMediaActivity.this.presenterImpl.getSelectedMediaObj() != null) {
                    Intent intent = new Intent(this, CropperActivity.class);
                    intent.putExtra(Constants.SELECTED_MEDIA_LIST_OBJ, SelectedMediaActivity.this.presenterImpl.getSelectedMediaObj());
                    startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE);
                }
                break;
            case R.id.action_save:
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_media);
        if (getIntent() != null && getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ) != null) {
            selectedMediaMap = (HashMap<String, MediaItemBean>) getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ);
            //reset isSelected parameter
            /*if (selectedMediaMap != null && selectedMediaMap.size() > 0) {
                for (Map.Entry<String, MediaItemBean> entry : selectedMediaMap.entrySet()) {
                    entry.getValue().setSelected(false);
                    selectedMediaMap.put(entry.getKey(), entry.getValue());
                }
            }*/
        }
        initViews();
        presenterImpl = new SelectedMediaPresenterImpl(this, this, selectedMediaMap);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        selectedViewPager = findViewById(R.id.selected_viewpager);
        selectedViewPager.setPageTransformer(false, new DetailViewPagerTransformer(DetailViewPagerTransformer.TransformType.DEPTH));
        selectedMediaRecycler = findViewById(R.id.recycler_selected_media);

//        singleMediaFragment = new SingleMediaFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.media_container, singleMediaFragment);
//        fragmentTransaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK) {
            if (requestCode == CROP_IMAGE_REQUEST_CODE) {
                if (data.getExtras() != null) {
                    Bitmap croppedBitmap = (Bitmap) data.getExtras().get("data");
                    System.out.println("Cropped Bitmap-> " + croppedBitmap);

                    System.out.println("on activity result called, save cropped image here");
                }
            }
        }*/
        if (presenterImpl != null)
            presenterImpl.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public ViewPager getSelectedViewPager() {
        return selectedViewPager;
    }

    @Override
    public RecyclerView getSelectedMediaRecycler() {
        return selectedMediaRecycler;
    }


}
