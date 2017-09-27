package com.imagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.imagepicker.R;
import com.imagepicker.model.MediaItemBean;
import com.imagepicker.ui.selectedMedia.SelectedMediaPresenter;

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

    public MediaPagerAdapter() {

    }

    public MediaPagerAdapter(Context context, List<MediaItemBean> medialist, SelectedMediaPresenter presenter) {
        this.context = context;
        this.mediaItemBeanList = medialist;
        this.presenter = presenter;
    }

    public List<MediaItemBean> getList() {
        if (mediaItemBeanList == null) mediaItemBeanList = new ArrayList<>();
        return mediaItemBeanList;
    }

    public void updateList(List<MediaItemBean> medialist) {
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
        final ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
        final SimpleDraweeView selectedImage = itemView.findViewById(R.id.selected_image);

        progressBar.setVisibility(View.GONE);

        Uri uri = Uri.fromFile(new File(mediaItemBeanList.get(position).getMediaPath()));


        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(selectedImage.getController())
                .setAutoPlayAnimations(false)
                .build();

        selectedImage.setController(controller);

        // Add view_selected_image.xml to ViewPager
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
