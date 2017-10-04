package com.imagepicker.ui.selectedMedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.model.MessageEvent;
import com.imagepicker.ui.cropper.CropperActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.DetailViewPagerTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaActivity extends AppCompatActivity implements SelectedMediaView {
    private SelectedMediaPresenterImpl presenterImpl;
    private Toolbar toolbar;
    private ViewPager selectedViewPager;
    private RecyclerView selectedMediaRecycler;
    public static final int CROP_IMAGE_REQUEST_CODE = 501;
    public MenuItem cropMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selected_image, menu);
        cropMenu = menu.findItem(R.id.action_crop);
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_media);
        SparseArray<MediaItemBean> selectedMediaMap = null;
        if (getIntent() != null && getIntent().getParcelableExtra(Constants.SELECTED_MEDIA_LIST_OBJ) != null) {
            MediaItemBean obj = getIntent().getParcelableExtra(Constants.SELECTED_MEDIA_LIST_OBJ);
            selectedMediaMap = obj.getSeelctedItemMap();
        }
        initViews();
        presenterImpl = new SelectedMediaPresenterImpl(this, this, selectedMediaMap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        /* Do something */
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
