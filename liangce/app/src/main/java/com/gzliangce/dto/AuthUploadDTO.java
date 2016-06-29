package com.gzliangce.dto;

/**
 * Created by leo on 16/1/22.
 * 资料认证 - 上传图片
 */
public class AuthUploadDTO extends BaseDTO {

    /**
     * link : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com/auth/2016/01/72d38b45a2fe3cc4b.png
     * value : /auth/2016/01/72d38b45a2fe70ea0c92cbdcfc83cc4b.png
     */

    private String link;
    private String value;

    public void setLink(String link) {
        this.link = link;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AuthUploadDTO{" +
                "link='" + link + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
