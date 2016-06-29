package com.gzliangce.dto;

/**
 * public filed
 * <p>
 * Created by Tony on 10/15/15.
 */
public class HeaderDTO extends BaseDTO {

    /**
     * url : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com/icon/2016/01/561e35826a1e0b1f3f0.png
     */

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "HeaderDTO{" +
                "url='" + url + '\'' +
                '}';
    }
}
