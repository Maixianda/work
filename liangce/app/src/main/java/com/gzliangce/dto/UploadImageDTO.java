package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aaron on 2/3/16.
 */
public class UploadImageDTO extends BaseDTO {
    @SerializedName("link")
    private String link;
    @SerializedName("imageId")
    private long imageId;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "UploadImageDTO{" +
                "link='" + link + '\'' +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}
