package com.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.selectedMedia.SelectedMediaPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * auther Anuj Sharma on 9/21/2017.
 */

public class SelectedMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MediaItemBean> mediaList;
    private SelectedMediaPresenter presenter;
    public int selectedItem = -1;

    public SelectedMediaAdapter(Context context, List<MediaItemBean> mediaList, SelectedMediaPresenter listener) {
        this.context = context;
        this.mediaList = mediaList;
        this.presenter = listener;
    }


    public List<MediaItemBean> getList() {
        if (mediaList == null) mediaList = new ArrayList<>();
        return mediaList;
    }

    public void updateList(List<MediaItemBean> mediaList) {
        this.mediaList = mediaList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_selected_media, null);
        return new SelectedMediaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectedMediaHolder) {
            SelectedMediaHolder vh = (SelectedMediaHolder) holder;
            MediaItemBean obj = mediaList.get(position);

            vh.imageView.setImageURI(Uri.fromFile(new File(obj.getMediaPath())));

            if (selectedItem == position) {
                vh.viewSelected.setVisibility(View.VISIBLE);
            } else {
                vh.viewSelected.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mediaList == null) ? 0 : mediaList.size();
    }

    private class SelectedMediaHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView imageView;
        private View viewSelected;

        private SelectedMediaHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            viewSelected = itemView.findViewById(R.id.selected_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (presenter != null) {
                        selectedItem = getLayoutPosition();
                        presenter.onMediaClick(mediaList.get(getLayoutPosition()), getLayoutPosition());
                    }
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