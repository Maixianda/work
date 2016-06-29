package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 16/3/18.
 * 文章详情
 */
public class NewsDetailInfo implements Parcelable{

    /**
     * content : <p>【公积金中心】</p><p>机构名称 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;地址</p><p>广州住房公积金管理中心越秀管理部 &nbsp; 广州市豪贤路193号9楼</p><p>广州住房公积金管理中心荔湾管理部 &nbsp;广州市荔湾区康王北路1024号（金禧大厦2层）</p><p>广州住房公积金管理中心海珠管理部 &nbsp;广州市昌岗东路272号2楼</p><p>广州住房公积金管理中心天河管理部 &nbsp;广州市珠江新城华就路12号三银大厦2楼</p><p>广州住房公积金管理中心白云管理部 &nbsp;广州市白云区机场路1438号正阳大厦2楼</p><p>广州住房公积金管理中心黄埔管理部 &nbsp; 广州市黄埔区黄埔东路302号黄埔花园裙楼2楼</p><p>广州住房公积金管理中心萝岗管理部 &nbsp; 广州市萝岗区开创大道北香雪二路2号 &nbsp;</p><p>广州住房公积金管理中心南沙管理部 &nbsp;广州市南沙区南沙街进港大道22号39单位（金莎广场2楼）</p><p>广州住房公积金管理中心番禺办事处 &nbsp;广州市番禺区德兴路37、39号</p><p>广州住房公积金管理中心花都办事处 &nbsp;广州市花都区新华街公益路21号信合大厦南座6楼</p><p>广州住房公积金管理中心从化办事处 &nbsp; 从化市街口镇河滨南路</p><p>广州住房公积金管理中心增城办事处 &nbsp; 增城市荔城街荔园路1号</p><p>广州住房公积金管理中心增城办事处新塘分理处 &nbsp;增城市新塘镇汇美新村公园街10号二楼</p><p>广州住房公积金管理中心铁路分中心（仅办理广铁单位与职工的公积金业务） 广州市中山一路101号首层</p><p>中心咨询电话：12329</p><p><br/></p>
     * summary : test16
     * id : 39
     * createTime : 2016-03-17
     * cover : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com/banner/banner1.png
     * title : test16
     * source : test16
     * keyword :
     */

    @SerializedName("content")
    private String content;
    @SerializedName("summary")
    private String summary;
    @SerializedName("id")
    private int id;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("cover")
    private String cover;
    @SerializedName("title")
    private String title;
    @SerializedName("source")
    private String source;
    @SerializedName("keyword")
    private String keyword;

    public void setContent(String content) {
        this.content = content;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getKeyword() {
        return keyword;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.summary);
        dest.writeInt(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.source);
        dest.writeString(this.keyword);
    }

    public NewsDetailInfo() {
    }

    protected NewsDetailInfo(Parcel in) {
        this.content = in.readString();
        this.summary = in.readString();
        this.id = in.readInt();
        this.createTime = in.readString();
        this.cover = in.readString();
        this.title = in.readString();
        this.source = in.readString();
        this.keyword = in.readString();
    }

    public static final Creator<NewsDetailInfo> CREATOR = new Creator<NewsDetailInfo>() {
        @Override
        public NewsDetailInfo createFromParcel(Parcel source) {
            return new NewsDetailInfo(source);
        }

        @Override
        public NewsDetailInfo[] newArray(int size) {
            return new NewsDetailInfo[size];
        }
    };


    @Override
    public String toString() {
        return "NewsDetailInfo{" +
                "content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", id=" + id +
                ", createTime='" + createTime + '\'' +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
