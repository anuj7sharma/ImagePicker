package com.imagepicker.adapter;

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
 * Created by Anuj Sharma on 9/18/2017.
 */

public class MediaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int lastPosition = -1;
    private List<MediaItemBean> mediaList;
    MediaListPresenter presenter;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_media_item, null);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaHolder) {
            MediaHolder vh = (MediaHolder) holder;
            MediaItemBean obj = (MediaItemBean) mediaList.get(position);
            Picasso.with(context).load(new File(obj.getMediaPath())).placeholder(R.mipmap.ic_launcher).resize(300, 300).centerCrop().into(vh.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return (mediaList == null) ? 0 : mediaList.size();
    }

    private class MediaHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View viewSelected;

        public MediaHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.media_image);
            viewSelected = (View) itemView.findViewById(R.id.selected_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onMediaItemClick(mediaList.get(getLayoutPosition()));
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    presenter.onMediaItemLongClick(mediaList.get(getLayoutPosition()), imageView);
                    return false;
                }
            });
        }
    }
}
