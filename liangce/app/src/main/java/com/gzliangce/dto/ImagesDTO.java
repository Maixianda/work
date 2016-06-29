package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.Supplement;

import java.util.List;

/**
 * Created by aaron on 2/18/16.
 */
public class ImagesDTO extends BaseDTO {

    @SerializedName("images")
    private List<Images> images;

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImagesDTO{" +
                "images=" + images +
                '}';
    }
}
