package com.imagepicker.model;

/**
 * Created by Anuj Sharma on 9/18/2017.
 */

public class MediaItemBean {
    boolean isSelected;
    String mediaName;
    String mediaExtenstion;
    String mediaSize;
    String mediaThumbnail;
    String mediaPath;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaExtenstion() {
        return mediaExtenstion;
    }

    public void setMediaExtenstion(String mediaExtenstion) {
        this.mediaExtenstion = mediaExtenstion;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
