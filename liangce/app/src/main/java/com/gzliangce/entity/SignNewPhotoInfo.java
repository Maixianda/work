package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/27.
 * 签约拍照 - 数据对象
 */
public class SignNewPhotoInfo implements Serializable {
    private String title;
    private String image;
    private String lable;
    private int position;
    private String orderNumber;

    public SignNewPhotoInfo(String title, String image, String lable) {
        this.title = title;
        this.image = image;
        this.lable = lable;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "SignNewPhotoInfo{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", lable='" + lable + '\'' +
                ", position=" + position +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
