package com.imagepicker.ui.selectedMedia;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.imagepicker.adapter.SelectedMediaAdapter;
import com.imagepicker.model.MediaItemBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaPresenterImpl implements SelectedMediaPresenter {

    private SelectedMediaActivity selectedMediaActivity;
    private SelectedMediaView selectedMediaView;
    private SingleMediaFragment singleMediaFragment;
    private HashMap<String, MediaItemBean> selectedMediaMap;
    private SelectedMediaAdapter adapter;

    public SelectedMediaPresenterImpl(SelectedMediaActivity selectedMediaActivity, SelectedMediaView selectedMediaView, HashMap<String, MediaItemBean> selectedMediaMap) {
        this.selectedMediaActivity = selectedMediaActivity;
        this.selectedMediaView = selectedMediaView;
        this.selectedMediaMap = selectedMediaMap;
        init();
    }

    private void init() {
        selectedMediaActivity.setSupportActionBar(selectedMediaView.getToolbar());
        selectedMediaActivity.getSupportActionBar().setTitle("Crop Image");
        selectedMediaActivity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        selectedMediaActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        selectedMediaView.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMediaActivity.finish();
            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(selectedMediaActivity);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectedMediaView.getSelectedMediaRecycler().setLayoutManager(lm);
        List<MediaItemBean> selectedMediaList = new ArrayList<>(selectedMediaMap.values());
        adapter = new SelectedMediaAdapter(selectedMediaActivity, selectedMediaList, this);
        selectedMediaView.getSelectedMediaRecycler().setAdapter(adapter);
    }

    @Override
    public void onMediaClick(MediaItemBean obj, int position) {
        selectedMediaView.getSelectedContainer().presenterImpl.showView();
    }

    @Override
    public void onMediaLongClick(MediaItemBean obj, int position, ImageView view) {

    }
}
