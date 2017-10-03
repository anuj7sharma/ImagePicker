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
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.adapter.MediaListAdapter;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.selectedMedia.SelectedMediaActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.PermissionsAndroid;
import com.imagepicker.utils.SpacesItemDecoration;

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
    private SparseArray<MediaItemBean> selectedMediaMap;
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
        // observer will receive all events.

        mediaListActivity.setSupportActionBar(mediaListView.getToolbar());
        mediaListActivity.getSupportActionBar().setTitle("");
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
        adapter = new MediaListAdapter(null, this);
        mediaListView.getRecyclerView().setAdapter(adapter);

        mediaListView.getFastScroller().setRecyclerView(mediaListView.getRecyclerView());
        mediaListView.getFastScroller().setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);


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
        if (selectedMediaMap == null) selectedMediaMap = new SparseArray<>();
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
                            selectedMediaMap.remove(Integer.parseInt(mediaItemBean.getId()));
                        } else {
                            mediaItemBean.setSelected(true);
                            selectedMediaMap.put(Integer.parseInt(mediaItemBean.getId()), mediaItemBean);
                        }
                        adapter.getList().set(position, mediaItemBean);
                        adapter.updateList(adapter.getList());
                        manageToolbarCount();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Error-> " + e.getMessage());
                    }
                });
        System.out.println("Select Id-> " + obj.getId());
    }

    private void manageToolbarCount() {
        if (selectedMediaMap == null) return;
        //update selected media count on toolbar
        mediaListActivity.count.setTitle(String.valueOf(selectedMediaMap.size()));
        if (selectedMediaMap.size() > 0) {
            mediaListActivity.count.setVisible(true);
            mediaListActivity.save.setVisible(true);
        } else {
            mediaListActivity.count.setVisible(false);
            mediaListActivity.save.setVisible(false);
        }
    }

    public void updateSelectedMedia(MediaItemBean mediaItemBean) {
        if (selectedMediaMap != null) {
            //Now remove selected Items from arraylist
            Single.just(mediaItemBean).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<MediaItemBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(MediaItemBean mediaItemBean) {
                            for (MediaItemBean obj :
                                    adapter.getList()) {

                            }
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                if (adapter.getList().get(i).getId().equals(mediaItemBean.getId())) {
                                    adapter.getList().remove(i);
                                }
                            }
                            selectedMediaMap.remove(Integer.parseInt(mediaItemBean.getId()));
                            adapter.updateList(adapter.getList());
                            manageToolbarCount();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }


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
                                        int dateAdded = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED);
                                        int mediaType = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
                                        int title = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE);
                                        int width = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH);
                                        int height = data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT);

//                                        String imagePath = data.getString(mediaData);
                                        MediaItemBean obj = new MediaItemBean();
                                        obj.setId(data.getString(id));
                                        obj.setMediaPath(data.getString(mediaData));
                                        obj.setMediaName(data.getString(mediaName));
                                        obj.setMimeType(data.getString(mimeType));
                                        obj.setMediaSize(data.getLong(mediaSize));
                                        obj.setMediaType(data.getString(mediaType));
                                        obj.setTitle(data.getString(title));
                                        obj.setWidth(data.getInt(width));
                                        obj.setHeight(data.getInt(height));
                                        obj.setDateAdded(data.getString(dateAdded));

                                        e.onNext(obj);
                                    }
                                    e.onComplete();
                                }
                            }).subscribeOn(Schedulers.computation());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
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
        MediaItemBean obj = new MediaItemBean();
        obj.setSeelctedItemMap(selectedMediaMap);
        //navigate selected media items to next screen.
        Intent intent = new Intent(mediaListActivity, SelectedMediaActivity.class);
        intent.putExtra(Constants.SELECTED_MEDIA_LIST_OBJ, obj);
        mediaListActivity.startActivity(intent);
    }
}
