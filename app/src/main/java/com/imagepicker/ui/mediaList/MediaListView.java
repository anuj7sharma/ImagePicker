package com.imagepicker.ui.mediaList;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.imagepicker.utils.RecyclerViewFastScroller;

/**
 * auther Anuj Sharma on 9/18/2017.
 */

public interface MediaListView {
    RecyclerView getRecyclerView();
    FloatingActionButton getCameraBtn();
    RecyclerViewFastScroller getFastScroller();
    Toolbar getToolbar();
    AppCompatSpinner getSpinner();
}
