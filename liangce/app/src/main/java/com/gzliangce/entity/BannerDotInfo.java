package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 15/12/9.
 * 首页广告banner dot对象
 */
public class BannerDotInfo implements Serializable {
    private boolean isSelected;

    public BannerDotInfo(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
