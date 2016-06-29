package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/5/23.
 * 公司
 */
public class CompanyInfo implements Serializable {
    @SerializedName("name")
    private String companyName;
    @SerializedName("id")
    private String companyId;
    @SerializedName("children")
    private List<DepartmentInfo> child;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<DepartmentInfo> getChild() {
        return child;
    }

    public void setChild(List<DepartmentInfo> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", child=" + child +
                '}';
    }
}
