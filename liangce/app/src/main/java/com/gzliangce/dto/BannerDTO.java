package com.gzliangce.dto;

import com.gzliangce.entity.BannerImageInfo;

import java.util.List;

/**
 * Created by leo on 16/2/23.
 * 广告数据对象
 */
public class BannerDTO extends BaseDTO {

    /**
     * id : 1
     * title : test
     * image : http://1.jpg
     * url : http://www.qq.com
     */

    private List<BannerImageInfo> list;

    public void setList(List<BannerImageInfo> list) {
        this.list = list;
    }

    public List<BannerImageInfo> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "BannerDTO{" +
                "list=" + list +
                '}';
    }
}
