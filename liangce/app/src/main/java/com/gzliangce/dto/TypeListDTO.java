package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsTypeInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 1/29/16.
 * 课程相关列表数据
 */
public class TypeListDTO extends BaseDTO implements Serializable {

    @SerializedName("typeList")
    private List<NewsTypeInfo> typeList;
    @SerializedName("courseList")
    private List<CourseInfo> courseList;

    public List<NewsTypeInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<NewsTypeInfo> typeList) {
        this.typeList = typeList;
    }

    public List<CourseInfo> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseInfo> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return "TypeListDTO{" +
                "typeList=" + typeList +
                ", courseList=" + courseList +
                '}';
    }
}
