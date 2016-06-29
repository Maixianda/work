package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aaron on 2/3/16.
 */
public class Images implements Serializable {

    /**
     * imageId : 1
     * url : /supplement/2016/01/a8041ef0598ae2f56cefd93a63b08361.png
     */
    @SerializedName("imageId")
    private long imageId;
    private String url;
    @SerializedName("id")
    private long id;
    private boolean uploaded = false;
    private boolean selected = false;
    private String picLabel;
    private String type;//签约拍照列表界面

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPicLabel() {
        return picLabel;
    }

    public void setPicLabel(String picLabel) {
        this.picLabel = picLabel;
    }

    @Override
    public String toString() {
        return "Images{" +
                "imageId=" + imageId +
                ", url='" + url + '\'' +
                ", id=" + id +
                ", uploaded=" + uploaded +
                ", selected=" + selected +
                ", picLabel='" + picLabel + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
