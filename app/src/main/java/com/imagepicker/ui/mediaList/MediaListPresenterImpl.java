package com.imagepicker.ui.mediaList;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.adapter.MediaListAdapter;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.selectedMedia.SelectedMediaActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.PermissionsAndroid;
import com.imagepicker.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * auther Anuj Sharma on 9/18/2017.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MediaListPresenterImpl implements MediaListPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    private MediaListActivity mediaListActivity;
    private MediaListView mediaListView;
    private List<MediaItemBean> mediaList;
    private HashMap<String, MediaItemBean> selectedMediaMap;
    private MediaListAdapter adapter;

    //Variables related to Media Projections
    // Get relevant columns for use later.
    private String[] projection = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,

    };
    // Return only video and image metadata.
    private String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    private Uri queryUri = MediaStore.Files.getContentUri("external");


    MediaListPresenterImpl(MediaListActivity mediaListActivity, MediaListView mediaListView) {
        this.mediaListActivity = mediaListActivity;
        this.mediaListView = mediaListView;
        init();
    }

    private void init() {
        mediaListActivity.setSupportActionBar(mediaListView.getToolbar());
        mediaListActivity.getSupportActionBar().setTitle(mediaListActivity.getString(R.string.app_name));
        mediaListActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mediaListView.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mediaListView.getRecyclerView().setLayoutManager(sm);
        int spacingInPixels = mediaListActivity.getResources().getDimensionPixelSize(R.dimen.margin_4);
        mediaListView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        adapter = new MediaListAdapter(mediaListActivity, null, this);
        mediaListView.getRecyclerView().setAdapter(adapter);
        fetchMediaFiles();
    }

    void fetchMediaFiles() {
        //check read/write storage permission
        if (PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(mediaListActivity)) {
            mediaListActivity.getLoaderManager().initLoader(0, null, this);
        } else {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(mediaListActivity);
        }
    }

    @Override
    public void onMediaItemLongClick(MediaItemBean obj, int positon, ImageView view) {
        //Navigate user to MediaDetail screen

    }

    @Override
    public void onMediaItemClick(MediaItemBean obj, int position) {
        //select
        if (selectedMediaMap == null) selectedMediaMap = new HashMap<>();
        try {
            if (obj.isSelected()) {
                obj.setSelected(false);
                selectedMediaMap.remove(obj.getId());
            } else {
                obj.setSelected(true);
                selectedMediaMap.put(obj.getId(), obj);
            }
            mediaList.set(position, obj);
            adapter.updateList(mediaList);


            //update selected media count on toolbar
            mediaListActivity.getSupportActionBar().setTitle(R.string.app_name);
            mediaListActivity.count.setTitle(String.valueOf(selectedMediaMap.size()));
            if (selectedMediaMap.size() > 0) {
                mediaListActivity.count.setVisible(true);
                mediaListActivity.save.setVisible(true);
            } else {
                mediaListActivity.count.setVisible(false);
                mediaListActivity.save.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Select Id-> " + obj.getId());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //        Cursor cursor = cursorLoader.loadInBackground();
        return new CursorLoader(
                mediaListActivity,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String imagePath;
        mediaList = new ArrayList<>();
        if (data != null) {

            while (data.moveToNext()) {
                int id = data.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int mediaData = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                int title = data.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                int mediaName = data.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int mimeType = data.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
//                int mediaType = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
                int mediaSize = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);
                imagePath = data.getString(mediaData);
                System.out.println("Path-> " + imagePath);
                MediaItemBean obj = new MediaItemBean();
                obj.setId(data.getString(id));
                obj.setMediaName(data.getString(mediaName));
                obj.setMediaPath(data.getString(mediaData));
                obj.setMimeType(data.getString(mimeType));
                obj.setMediaSize(data.getLong(mediaSize));
                mediaList.add(obj);
            }
            if (mediaList.size() > 0) {
                updateMediaList(mediaList);
            }

        }
    }

    private void updateMediaList(List<MediaItemBean> mediaList) {
        adapter.updateList(mediaList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void saveSelectedMedia() {
        //navigate selected media items to next screen.
        Intent intent = new Intent(mediaListActivity, SelectedMediaActivity.class);
        intent.putExtra(Constants.SELECTED_MEDIA_LIST_OBJ, selectedMediaMap);
        mediaListActivity.startActivity(intent);
    }
}
