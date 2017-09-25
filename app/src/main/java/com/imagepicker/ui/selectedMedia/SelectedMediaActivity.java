package com.imagepicker.ui.selectedMedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.cropper.CropperActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.DetailViewPagerTransformer;

import java.util.HashMap;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaActivity extends AppCompatActivity implements SelectedMediaView {
    private SelectedMediaPresenterImpl presenterImpl;
    private HashMap<String, MediaItemBean> selectedMediaMap;
    private Toolbar toolbar;
    private ViewPager selectedViewPager;
    private RecyclerView selectedMediaRecycler;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selected_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_crop:
                //Move to Seperate Activity for Cropping
                if (SelectedMediaActivity.this.presenterImpl.getSelectedMediaObj() != null) {
                    Intent intent = new Intent(this, CropperActivity.class);
                    intent.putExtra(Constants.SELECTED_MEDIA_LIST_OBJ, SelectedMediaActivity.this.presenterImpl.getSelectedMediaObj());
                    startActivity(intent);
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
