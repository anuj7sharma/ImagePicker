package com.imagepicker.ui.selectedMedia;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * Created by Anuj Sharma on 9/21/2017.
 */

public interface SelectedMediaView {
    Toolbar getToolbar();

    RecyclerView getSelectedMediaRecycler();

    SingleMediaFragment getSelectedContainer();
}
