package com.imagepicker.ui.mediaList;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.adapter.MediaListAdapter;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.utils.PermissionsAndroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anuj Sharma on 9/18/2017.
 */

public class MediaListPresenterImpl implements MediaListPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    private MediaListActivity mediaListActivity;
    private MediaListView mediaListView;
    private MediaListAdapter adapter;

    //Variables related to Media Projections
    // Get relevant columns for use later.
    String[] projection = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,

    };
    // Return only video and image metadata.
    String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    Uri queryUri = MediaStore.Files.getContentUri("external");


    MediaListPresenterImpl(MediaListActivity mediaListActivity, MediaListView mediaListView) {
        this.mediaListActivity = mediaListActivity;
        this.mediaListView = mediaListView;
        init();
    }

    private void init() {
        mediaListActivity.setSupportActionBar(mediaListView.getToolbar());
        mediaListActivity.getSupportActionBar().setTitle(mediaListActivity.getString(R.string.app_name));

        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        sm.setAutoMeasureEnabled(true);
        mediaListView.getRecyclerView().setLayoutManager(sm);
        adapter = new MediaListAdapter(mediaListActivity, null, this);
        mediaListView.getRecyclerView().setAdapter(adapter);
        fetchMediaFiles();
    }

    public void fetchMediaFiles() {
        //check read/write storage permission
        if (PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(mediaListActivity)) {
            mediaListActivity.getLoaderManager().initLoader(0, null, this);
        } else {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(mediaListActivity);
        }
    }

    @Override
    public void onMediaItemLongClick(MediaItemBean obj, ImageView view) {
        //Navigate user to MediaDetail screen

    }

    @Override
    public void onMediaItemClick(MediaItemBean obj) {
        //select
    }

    @Override
    public int totalSelectedItemCount() {
        return 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(
                mediaListActivity,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        Cursor cursor = cursorLoader.loadInBackground();
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String imagePath;
        List<MediaItemBean> mediaList = new ArrayList<>();
        if (data != null) {

            while (data.moveToNext()) {
                int mediaData = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int mediaName = data.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int mimeType = data.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
                int mediaType = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);

                imagePath = data.getString(mediaData);
                System.out.println("Path-> " + imagePath);
                MediaItemBean obj = new MediaItemBean();
                obj.setMediaName(data.getString(mediaName));
                obj.setMediaPath(data.getString(mediaData));
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
}
