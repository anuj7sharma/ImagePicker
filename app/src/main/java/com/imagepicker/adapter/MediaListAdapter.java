package com.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.mediaList.MediaListPresenter;
import com.imagepicker.utils.RecyclerViewFastScroller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * auther Anuj Sharma on 9/18/2017.
 */

public class MediaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private List<MediaItemBean> mediaList;
    private MediaListPresenter presenter;

    public MediaListAdapter(List<MediaItemBean> mediaList, MediaListPresenter listener) {
        this.mediaList = mediaList;
        this.presenter = listener;
    }

    public void updateList(List<MediaItemBean> mediaList) {
        this.mediaList = mediaList;
        notifyDataSetChanged();
    }

    public List<MediaItemBean> getList() {
        return (mediaList == null) ? mediaList = new ArrayList<>() : mediaList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_media_item, null);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaHolder) {
            MediaHolder vh = (MediaHolder) holder;
            MediaItemBean obj = mediaList.get(position);

            Uri uri = Uri.fromFile(new File(obj.getMediaPath()));
            vh.imageView.setImageURI(uri);
            /*ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(150, 150))
                    .setProgressiveRenderingEnabled(true)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(vh.imageView.getController())
                    .setAutoPlayAnimations(false)
                    .build();
//            boolean inMemoryCache = Fresco.getImagePipeline().isInBitmapMemoryCache(uri);
            vh.imageView.setController(controller);*/

            if (obj.isSelected()) {
                vh.selectedIcon.setVisibility(View.VISIBLE);
            } else {
                vh.selectedIcon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mediaList == null) ? 0 : mediaList.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
//        return (mediaList == null) ? "" : mediaList.get(pos).getMediaType();
        return "";
    }

    private class MediaHolder extends RecyclerView.ViewHolder {
        private ImageView selectedIcon;
        private SimpleDraweeView imageView;

        MediaHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            selectedIcon = itemView.findViewById(R.id.img_selected);

//            RxView.clicks(imageView).throttleFirst(200, TimeUnit.MICROSECONDS).
//                    subscribe(new Consumer<Object>() {
//
//                        @Override
//                        public void accept(Object o) throws Exception {
//                            presenter.onMediaItemClick(mediaList.get(getLayoutPosition()), getLayoutPosition());
//                        }
//                    });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onMediaItemClick(mediaList.get(getLayoutPosition()), getLayoutPosition());
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    presenter.onMediaItemLongClick(mediaList.get(getLayoutPosition()), getLayoutPosition(), imageView);
                    return false;
                }
            });
        }
    }

}
