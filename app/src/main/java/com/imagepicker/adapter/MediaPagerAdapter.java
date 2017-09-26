package com.imagepicker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.selectedMedia.SelectedMediaPresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author by Anuj Sharma on 9/25/2017.
 */

public class MediaPagerAdapter extends PagerAdapter {
    Context context;
    List<MediaItemBean> mediaItemBeanList;
    SelectedMediaPresenter presenter;
    public MediaPagerAdapter(){

    }
    public MediaPagerAdapter(Context context, List<MediaItemBean> medialist, SelectedMediaPresenter presenter) {
        this.context = context;
        this.mediaItemBeanList = medialist;
        this.presenter = presenter;
    }
    public List<MediaItemBean> getList(){
        if(mediaItemBeanList==null)mediaItemBeanList = new ArrayList<>();
        return mediaItemBeanList;
    }
    public void updateList(List<MediaItemBean> medialist){
        this.mediaItemBeanList = medialist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mediaItemBeanList == null) ? 0 : mediaItemBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.view_selected_image, container, false);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        final ImageView selectedImage = itemView.findViewById(R.id.selected_image);
        /*Picasso.with(context).load(new File(mediaItemBeanList.get(position).getMediaPath())).into(selectedImage, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                selectedImage.setImageResource(R.drawable.ic_def_image);
                progressBar.setVisibility(View.GONE);
            }
        });*/
        Glide.with(context).load(new File(mediaItemBeanList.get(position).getMediaPath()))
                .into(selectedImage)
                .setRequest(new Request() {
                    @Override
                    public void begin() {

                    }

                    @Override
                    public void pause() {

                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public boolean isPaused() {
                        return false;
                    }

                    @Override
                    public boolean isRunning() {
                        return false;
                    }

                    @Override
                    public boolean isComplete() {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean isResourceSet() {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isFailed() {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public void recycle() {

                    }

                    @Override
                    public boolean isEquivalentTo(Request other) {
                        return false;
                    }
                });


        // Add view_selected_image.xml to ViewPager
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
