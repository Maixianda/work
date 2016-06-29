package com.gzliangce.entity;

import java.io.File;
import java.io.Serializable;

/**
 * Created by leo on 16/1/15.
 * 资格认证 - 本地照片数据
 */
public class QualificationImageInfo implements Serializable {
    private File file;
    private String flag;
    private String imageType;
    private String imageUrl;

    public QualificationImageInfo(File file, String flag, String imageType, String imageUrl) {
        this.file = file;
        this.flag = flag;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "QualificationImageInfo{" +
                "file=" + file +
                ", flag='" + flag + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
