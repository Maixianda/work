package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/3/9.
 * 资讯列表数据实体类
 */
public class NewsInfo implements Parcelable {

    /**
     * summary : test5
     * id : 29
     * cover : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com/banner/banner1.png
     * title : test5
     */

    @SerializedName("summary")
    private String summary;
    @SerializedName("id")
    private int id;
    @SerializedName("cover")
    private String cover;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.title);
    }

    public NewsInfo() {
    }

    protected NewsInfo(Parcel in) {
        this.summary = in.readString();
        this.id = in.readInt();
        this.cover = in.readString();
        this.title = in.readString();
    }

    public static final Creator<NewsInfo> CREATOR = new Creator<NewsInfo>() {
        @Override
        public NewsInfo createFromParcel(Parcel source) {
            return new NewsInfo(source);
        }

        @Override
        public NewsInfo[] newArray(int size) {
            return new NewsInfo[size];
        }
    };

    @Override
    public String toString() {
        return "NewsInfo{" +
                "summary='" + summary + '\'' +
                ", id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
