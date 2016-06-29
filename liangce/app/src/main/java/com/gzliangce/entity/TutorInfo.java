package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/3/28.
 * 导师详细信息
 */
public class TutorInfo implements Serializable {

    /**
     * tutorId : 1
     * name : 大师兄
     * photo : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com/tutorPhoto/2016/03/28/19821800a1e1018e723067e22f1025f4.png
     * status : 1
     * desc : 孙悟空
     */

    @SerializedName("tutorId")
    private int tutorId;
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String photo;
    @SerializedName("status")
    private int status;
    @SerializedName("desc")
    private String desc;

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "TutorInfo{" +
                "tutorId=" + tutorId +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
}
