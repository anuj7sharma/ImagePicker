package com.imagepicker.ui.mediaList;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.imagepicker.R;
import com.imagepicker.adapter.FolderSpinnerAdapter;
import com.imagepicker.adapter.MediaListAdapter;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.PickerActivity;
import com.imagepicker.ui.camera.CameraActivity;
import com.imagepicker.ui.selectedMedia.SelectedMediaActivity;
import com.imagepicker.utils.Constants;
import com.imagepicker.utils.PermissionsAndroid;
import com.imagepicker.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashSet;
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
public class MediaListPresenterImpl implements MediaListPresenter, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private MediaListActivity mediaListActivity;
    private MediaListView mediaListView;
    private SparseArray<MediaItemBean> selectedMediaMap;
    private List<MediaItemBean> mediaItemList;
    private HashSet<String> folders;
    private MediaListAdapter adapter;
    private FolderSpinnerAdapter folderSpinnerAdapter;

    //Variables related to Media Projections

    String allMediaSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

//    String imageFolderSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
//            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + "AND" + MediaStore.Files.FileColumns.;

    String imageSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

    String videoSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

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
                mediaListActivity.finish();
            }
        });
        mediaListView.getCameraBtn().setOnClickListener(this);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mediaListView.getRecyclerView().setLayoutManager(sm);
        mediaListView.getRecyclerView().setHasFixedSize(true);
        mediaListView.getRecyclerView().setItemViewCacheSize(20);
        mediaListView.getRecyclerView().setDrawingCacheEnabled(true);
        mediaListView.getRecyclerView().setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        int spacingInPixels = mediaListActivity.getResources().getDimensionPixelSize(R.dimen.margin_4);
        mediaListView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        adapter = new MediaListAdapter(null, this);
        mediaListView.getRecyclerView().setAdapter(adapter);

        mediaListView.getFastScroller().setRecyclerView(mediaListView.getRecyclerView());
        mediaListView.getFastScroller().setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
        mediaListView.getFastScroller().setVisibility(View.GONE);
        mediaListView.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long l) {
                switch (parent.getId()) {
                    case R.id.spinner_folder:
                        if (folders != null && folders.size() > 0) {
                            //load media for selected folder
                            Single.just(mediaItemList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DisposableSingleObserver<List<MediaItemBean>>() {
                                        @Override
                                        public void onSuccess(List<MediaItemBean> mediaItemBeans) {
                                            List<MediaItemBean> SelectedFolderMediaList = new ArrayList<>();
                                            for (MediaItemBean obj :
                                                    mediaItemList) {
                                                if (obj.getMediaPath().contains(folderSpinnerAdapter.getItem(position))) {
                                                    SelectedFolderMediaList.add(obj);
                                                }
                                            }
                                            if (SelectedFolderMediaList.size() > 25) {
                                                mediaListView.getFastScroller().setVisibility(View.VISIBLE);
                                            } else {
                                                mediaListView.getFastScroller().setVisibility(View.GONE);
                                            }
                                            adapter.updateList(SelectedFolderMediaList);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                        adapter.notifyItemChanged(position);
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

                            for (int i = 0; i < adapter.getList().size(); i++) {
                                if (adapter.getList().get(i).getId().equals(mediaItemBean.getId())) {
                                    adapter.getList().get(i).setSelected(false);
                                    adapter.notifyItemChanged(i);
                                }
                                if (mediaItemList.get(i).getId().equals(mediaItemBean.getId())) {
                                    mediaItemList.get(i).setSelected(false);
                                }
                            }
                            selectedMediaMap.remove(Integer.parseInt(mediaItemBean.getId()));

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
        return new CursorLoader(
                mediaListActivity,
                queryUri,
                projection,
                imageSelection,
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

                                        //Add folder name
                                        if (folders == null) folders = new HashSet<>();
                                        folders.add(data.getString(mediaData).substring(0, data.getString(mediaData).lastIndexOf('/')));


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
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toList()
                    .subscribe(new DisposableSingleObserver<List<MediaItemBean>>() {
                        @Override
                        public void onSuccess(List<MediaItemBean> mediaItemBeans) {
                            if (folders != null && folders.size() > 0) {
                                for (String str : folders) {
                                    System.out.println("Folder-> " + str);
                                }
                            }
                            if(folders==null){
                                showEmptyView();
                                return;
                            }
                            hideEmptyView();
                            List<String> foldersList = new ArrayList<String>(folders);
                            folderSpinnerAdapter = new FolderSpinnerAdapter(mediaListActivity, R.layout.spinner_item, R.id.category_name, foldersList);
                            mediaListView.getSpinner().setAdapter(folderSpinnerAdapter);

                            mediaItemList = mediaItemBeans;

                        }

                        private void showEmptyView() {
                            mediaListView.getEmptyView().setVisibility(View.VISIBLE);
                            mediaListView.getSpinner().setVisibility(View.GONE);
                        }
                        private void hideEmptyView(){
                            mediaListView.getEmptyView().setVisibility(View.GONE);
                            mediaListView.getSpinner().setVisibility(View.VISIBLE);
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
        mediaListActivity.startActivityForResult(intent, PickerActivity.PICKER_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_camera:
                if (checkCameraHardware(mediaListActivity))
                    mediaListActivity.startActivity(new Intent(mediaListActivity, CameraActivity.class));
                else
                    Toast.makeText(mediaListActivity, "Camera not found", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}
