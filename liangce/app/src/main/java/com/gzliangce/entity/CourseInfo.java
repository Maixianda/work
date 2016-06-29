package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/1/20.
 * 课程数据对象
 */
public class CourseInfo implements Serializable {
    private String icon;
    private String title;
    @SerializedName("courseName")
    private String courseName;
    private String teacherName;
    private String detail;
    private String picUrl;
    private String vedioUrl;

    @SerializedName("courseId")
    private int courseId;
    @SerializedName("description")
    private String description;
    @SerializedName("cover")
    private String cover;
    @SerializedName("url")
    private String url;
    @SerializedName("createTime")
    private long createTime;
    @SerializedName("form")
    private String form;

    @SerializedName("logo")
    private String logo;
    @SerializedName("typeName")
    private String typeName;
    @SerializedName("tutorName")
    private String tutorName;

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", detail='" + detail + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", vedioUrl='" + vedioUrl + '\'' +
                ", courseId='" + courseId + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", createTime=" + createTime +
                ", form='" + form + '\'' +
                ", logo='" + logo + '\'' +
                ", typeName='" + typeName + '\'' +
                ", tutorName='" + tutorName + '\'' +
                '}';
    }
}