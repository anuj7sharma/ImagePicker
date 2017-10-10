package com.imagepicker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.mediaList.MediaListActivity;
import com.imagepicker.utils.Constants;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class PickerActivity extends AppCompatActivity {
    public static final int PICKER_REQUEST_CODE = 1000;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_picker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getString(R.string.app_name));
        AppCompatButton btnGallery = findViewById(R.id.btn_gallery);
        txtView = findViewById(R.id.textView);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PickerActivity.this, MediaListActivity.class), PICKER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICKER_REQUEST_CODE) {
                ArrayList<MediaItemBean> selectedList = data.getParcelableArrayListExtra(Constants.SelectedMediaObj);
                for (MediaItemBean obj :
                        selectedList) {
                    txtView.append(obj.getMediaPath() + "\n");
                }
            }
        }
    }
}
