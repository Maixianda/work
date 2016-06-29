package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.NewsDetailInfo;

/**
 * Created by leo on 16/3/18.
 * 资讯详情
 */
public class NewsDetailDTO extends BaseDTO {
    @SerializedName("detail")
    private NewsDetailInfo detail;

    public NewsDetailInfo getDetail() {
        return detail;
    }

    public void setDetail(NewsDetailInfo detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "NewsDetailDTO{" +
                "detail=" + detail +
                '}';
    }
}
