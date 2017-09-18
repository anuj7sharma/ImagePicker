package com.imagepicker.ui.mediaList;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.PermissionsAndroid;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Anuj Sharma on 9/18/2017.
 */

public class MediaListActivity extends AppCompatActivity implements MediaListView {
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private MediaListPresenterImpl presenterImpl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        initViews();
        presenterImpl = new MediaListPresenterImpl(this, this);

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.media_recycler);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsAndroid.CAMERA_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Show option to update profile
                   if(presenterImpl!=null){
                       presenterImpl.fetchMediaFiles();
                   }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Camera/Storage permission Denied", Toast.LENGTH_SHORT).show();
//                    Utils.getInstance().showToast("Camera/Storage permission Denied");
                }
                return;
            }

        }
    }
}
