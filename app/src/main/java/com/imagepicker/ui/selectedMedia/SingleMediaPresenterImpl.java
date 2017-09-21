package com.imagepicker.ui.selectedMedia;

import android.widget.Toast;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SingleMediaPresenterImpl implements SingleMediaPresenter {
    private SingleMediaFragment fragment;
    private SingleMediaView view;

    SingleMediaPresenterImpl(SingleMediaFragment fragment, SingleMediaView view) {
        this.fragment = fragment;
        this.view = view;
        init();
    }

    private void init() {

    }

    void showView() {
        Toast.makeText(fragment.getActivity(), "show view", Toast.LENGTH_SHORT).show();
    }
}
