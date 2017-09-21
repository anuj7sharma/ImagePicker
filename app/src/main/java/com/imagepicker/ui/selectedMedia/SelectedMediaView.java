package com.imagepicker.ui.selectedMedia;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public interface SelectedMediaView {
    Toolbar getToolbar();

    RecyclerView getSelectedMediaRecycler();

    SingleMediaFragment getSelectedContainer();
}
