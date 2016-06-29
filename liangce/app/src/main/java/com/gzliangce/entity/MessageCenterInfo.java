package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/2/24.
 * 消息中心 - 数据对象
 */
public class MessageCenterInfo implements Serializable {

    /**
     * id : 1
     * title : test
     * content : testttt
     * createTime : 1456109039000
     */

    private int id;
    private String title;
    private String content;
    @SerializedName("createTime")
    private long createTime;

    public MessageCenterInfo(long createTime) {
        this.createTime = createTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreateTime() {
        return createTime;
    }
}
