package com.imagepicker.ui.selectedMedia;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.adapter.MediaPagerAdapter;
import com.imagepicker.adapter.SelectedMediaAdapter;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaPresenterImpl implements SelectedMediaPresenter {

    private SelectedMediaActivity selectedMediaActivity;
    private SelectedMediaView selectedMediaView;
    private HashMap<String, MediaItemBean> selectedMediaMap;

    private SelectedMediaAdapter adapter;

    private PagerAdapter pagerAdapter;

    SelectedMediaPresenterImpl(SelectedMediaActivity selectedMediaActivity, SelectedMediaView selectedMediaView, HashMap<String, MediaItemBean> selectedMediaMap) {
        this.selectedMediaActivity = selectedMediaActivity;
        this.selectedMediaView = selectedMediaView;
        this.selectedMediaMap = selectedMediaMap;
        init();
    }

    private void init() {
        selectedMediaActivity.setSupportActionBar(selectedMediaView.getToolbar());
        selectedMediaActivity.getSupportActionBar().setTitle(R.string.title_crop_image);
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
        int spacingInPixels = selectedMediaActivity.getResources().getDimensionPixelSize(R.dimen.margin_4);
        selectedMediaView.getSelectedMediaRecycler().addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        List<MediaItemBean> selectedMediaList = new ArrayList<>(selectedMediaMap.values());
        adapter = new SelectedMediaAdapter(selectedMediaActivity, selectedMediaList, this);
        selectedMediaView.getSelectedMediaRecycler().setAdapter(adapter);
        adapter.selectedItem = 0;
        adapter.notifyDataSetChanged();


        selectedMediaView.getSelectedViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adapter != null) {
                    //change adapter current position
                    adapter.selectedItem = position;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //set pager pagerAdapter
        pagerAdapter = new MediaPagerAdapter(selectedMediaActivity,
                new ArrayList<>(selectedMediaMap.values()), this);
        selectedMediaView.getSelectedViewPager().setAdapter(pagerAdapter);
    }

    public MediaItemBean getSelectedMediaObj() {
        if (selectedMediaView.getSelectedViewPager() != null && adapter != null) {
            return adapter.getList().get(selectedMediaView.getSelectedViewPager().getCurrentItem());
        }
        return null;
    }

    @Override
    public void onMediaClick(MediaItemBean obj, int position) {
        if (obj != null) {
            /*if (obj.isSelected()) {
                obj.setSelected(false);
            } else {
                obj.setSelected(true);
            }*/
//            adapter.getList().set(position, obj);
//            adapter.updateList(adapter.getList());
            selectedMediaView.getSelectedViewPager().setCurrentItem(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMediaLongClick(MediaItemBean obj, int position, ImageView view) {

    }

    @Override
    public void onMediaUpSwipe() {

    }

    @Override
    public void onMediaDownSwipe() {

    }
}
