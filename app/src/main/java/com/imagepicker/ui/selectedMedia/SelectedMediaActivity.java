package com.imagepicker.ui.selectedMedia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.Constants;

import java.util.HashMap;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaActivity extends AppCompatActivity implements SelectedMediaView {

    private HashMap<String, MediaItemBean> selectedMediaMap;
    private Toolbar toolbar;
    private RecyclerView selectedMediaRecycler;
    private SingleMediaFragment singleMediaFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_media);
        if (getIntent() != null && getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ) != null) {
            selectedMediaMap = (HashMap<String, MediaItemBean>) getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ);
        }
        initViews();
        SelectedMediaPresenterImpl presenterImpl = new SelectedMediaPresenterImpl(this, this, selectedMediaMap);
    }

    private void initViews() {
        toolbar =  findViewById(R.id.toolbar);
        selectedMediaRecycler =  findViewById(R.id.recycler_selected_media);

        singleMediaFragment = new SingleMediaFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.media_container, singleMediaFragment);
        fragmentTransaction.commit();

    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public RecyclerView getSelectedMediaRecycler() {
        return selectedMediaRecycler;
    }

    @Override
    public SingleMediaFragment getSelectedContainer() {
        return singleMediaFragment;
    }
}
