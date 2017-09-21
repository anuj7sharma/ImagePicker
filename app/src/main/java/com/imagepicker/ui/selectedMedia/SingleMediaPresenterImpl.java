package com.imagepicker.ui.selectedMedia;

import android.widget.Toast;

/**
 * Created by Anuj Sharma on 9/21/2017.
 */

public class SingleMediaPresenterImpl implements SingleMediaPresenter {
    private SingleMediaFragment fragment;
    private SingleMediaView view;

    public SingleMediaPresenterImpl(SingleMediaFragment fragment, SingleMediaView view) {
        this.fragment = fragment;
        this.view = view;
        init();
    }

    private void init() {

    }

    public void showView() {
        Toast.makeText(fragment.getActivity(), "show view", Toast.LENGTH_SHORT).show();
    }
}
