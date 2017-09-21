package com.imagepicker.model;

import java.io.Serializable;

/**
 * Created by Anuj Sharma on 9/18/2017.
 */

public class MediaItemBean implements Serializable{
    boolean isSelected;
    String id;
    String mediaName;
    String mediaExtenstion;
    long mediaSize;
    String mediaPath;
    String mimeType;

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

    public long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
