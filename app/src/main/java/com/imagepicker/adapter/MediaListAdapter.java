package com.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.mediaList.MediaListPresenter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * auther Anuj Sharma on 9/18/2017.
 */

public class MediaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MediaItemBean> mediaList;
    private MediaListPresenter presenter;

    public MediaListAdapter(Context context, List<MediaItemBean> mediaList, MediaListPresenter listener) {
        this.context = context;
        this.mediaList = mediaList;
        this.presenter = listener;
    }

    public void updateList(List<MediaItemBean> mediaList) {
        this.mediaList = mediaList;
        notifyDataSetChanged();
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
            MediaItemBean obj =  mediaList.get(position);
            Picasso.with(context).load(new File(obj.getMediaPath())).placeholder(R.drawable.ic_def_image).resize(300, 300).centerCrop().into(vh.imageView);
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

    private class MediaHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, selectedIcon;

        MediaHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.media_image);
            selectedIcon =  itemView.findViewById(R.id.img_selected);

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
