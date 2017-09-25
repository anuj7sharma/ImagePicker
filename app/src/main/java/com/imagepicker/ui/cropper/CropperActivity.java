package com.imagepicker.ui.cropper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * author by Anuj Sharma on 9/25/2017.
 */

public class CropperActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        menu.findItem(R.id.action_count).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        initViews();
        if (getIntent() != null && getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ) != null) {
            MediaItemBean mediaObj = (MediaItemBean) getIntent().getSerializableExtra(Constants.SELECTED_MEDIA_LIST_OBJ);
            Picasso.with(this).load(new File(mediaObj.getMediaPath())).into((ImageView) findViewById(R.id.crop_image));

        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Crop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
