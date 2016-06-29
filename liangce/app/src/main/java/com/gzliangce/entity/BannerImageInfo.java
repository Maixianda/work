package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 15/12/9.
 * 头部广告banner 数据对象
 */
public class BannerImageInfo implements Serializable {
    private String url;
    private int id;
    private String title;
    private String image;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BannerImageInfo(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerImageInfo{" +
                "url='" + url + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
