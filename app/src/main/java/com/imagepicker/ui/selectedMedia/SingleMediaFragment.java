package com.imagepicker.ui.selectedMedia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagepicker.R;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SingleMediaFragment extends Fragment implements SingleMediaView {
    private View rootView;
    private ImageView imageView;
    public SingleMediaPresenterImpl presenterImpl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_selected_media, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = rootView.findViewById(R.id.single_image);
        presenterImpl = new SingleMediaPresenterImpl(this, this);
    }

    @Override
    public ImageView getImage() {
        return imageView;
    }
}
