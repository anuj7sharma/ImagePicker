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
import com.imagepicker.ui.selectedMedia.SelectedMediaPresenter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MediaItemBean> mediaList;
    private SelectedMediaPresenter presenter;

    public SelectedMediaAdapter(Context context, List<MediaItemBean> mediaList, SelectedMediaPresenter listener) {
        this.context = context;
        this.mediaList = mediaList;
        this.presenter = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_media_item, null);
        return new SelectedMediaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectedMediaHolder) {
            SelectedMediaHolder vh = (SelectedMediaHolder) holder;
            MediaItemBean obj = mediaList.get(position);
            Picasso.with(context).load(new File(obj.getMediaPath())).placeholder(R.drawable.ic_def_image).resize(300, 500).centerCrop().into(vh.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return (mediaList == null) ? 0 : mediaList.size();
    }

    private class SelectedMediaHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, selectedIcon;
        private View viewSelected;

        private  SelectedMediaHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.media_image);
            selectedIcon =  itemView.findViewById(R.id.img_selected);
            viewSelected =  itemView.findViewById(R.id.selected_view);
            selectedIcon.setVisibility(View.GONE);
            viewSelected.setVisibility(View.GONE);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (presenter != null)
                        presenter.onMediaClick(mediaList.get(getLayoutPosition()), getLayoutPosition());
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (presenter != null)
                        presenter.onMediaLongClick(mediaList.get(getLayoutPosition()), getLayoutPosition(), imageView);
                    return false;
                }
            });
        }
    }
}