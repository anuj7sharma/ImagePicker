package com.imagepicker.ui.mediaList;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * auther Anuj Sharma on 9/18/2017.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MediaListPresenterImpl implements MediaListPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    private MediaListActivity mediaListActivity;
    private MediaListView mediaListView;
    //    private List<MediaItemBean> mediaList;
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
    public void onMediaItemClick(final MediaItemBean obj, final int position) {
        //select
        if (selectedMediaMap == null) selectedMediaMap = new HashMap<>();
        Single.just(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MediaItemBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("on subscribe-> " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(MediaItemBean mediaItemBean) {
                        if (mediaItemBean.isSelected()) {
                            mediaItemBean.setSelected(false);
                            selectedMediaMap.remove(mediaItemBean.getId());
                        } else {
                            mediaItemBean.setSelected(true);
                            selectedMediaMap.put(mediaItemBean.getId(), mediaItemBean);
                        }
                        adapter.getList().set(position, mediaItemBean);
                        adapter.updateList(adapter.getList());

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
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Error-> " + e.getMessage());
                    }
                });
        System.out.println("Select Id-> " + obj.getId());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //        Cursor cursor = cursorLoader.loadInBackground();
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
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
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data != null)
            Single.just(data)
                    .flatMapObservable(new Function<Cursor, ObservableSource<MediaItemBean>>() {
                        @Override
                        public ObservableSource<MediaItemBean> apply(final Cursor data) throws Exception {
                            return Observable.create(new ObservableOnSubscribe<MediaItemBean>() {
                                @Override
                                public void subscribe(ObservableEmitter<MediaItemBean> e) throws Exception {
                                    while (data.moveToNext()) {
                                        int id = data.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                                        int mediaData = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                        int mediaName = data.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                                        int mimeType = data.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
                                        int mediaSize = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);
                                        String imagePath = data.getString(mediaData);
                                        System.out.println("Path-> " + imagePath);
                                        MediaItemBean obj = new MediaItemBean();
                                        obj.setId(data.getString(id));
                                        obj.setMediaName(data.getString(mediaName));
                                        obj.setMediaPath(data.getString(mediaData));
                                        obj.setMimeType(data.getString(mimeType));
                                        obj.setMediaSize(data.getLong(mediaSize));
                                        e.onNext(obj);
                                    }
                                    e.onComplete();
                                }
                            }).subscribeOn(Schedulers.computation());
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .toList()
                    .subscribe(new DisposableSingleObserver<List<MediaItemBean>>() {
                        @Override
                        public void onSuccess(List<MediaItemBean> mediaItemBeans) {
                            adapter.getList().addAll(mediaItemBeans);
                            adapter.updateList(adapter.getList());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
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
