package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/5/23.
 * 分部
 */
public class DepartmentInfo implements Serializable {
    @SerializedName("name")
    private String departmentName;
    @SerializedName("id")
    private String departmentId;

    public DepartmentInfo(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "DepartmentInfo{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}
