package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 1/25/16.
 */
public class AreaList implements Serializable {

    /**
     * areaId : 110000
     * name : 北京
     * children : [{"areaId":110100,"name":"北京市","children":[{"areaId":110101,"name":"东城区"},{"areaId":110102,"name":"西城区"}]}]
     */
    @SerializedName("areaId")
    private long areaId;
    private String name;
    /**
     * areaId : 110100
     * name : 北京市
     * children : [{"areaId":110101,"name":"东城区"},{"areaId":110102,"name":"西城区"}]
     */
    @SerializedName("children")
    private List<AreaList> children;

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaList> getChildren() {
        return children;
    }

    public void setChildren(List<AreaList> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "AreaList{" +
                "areaId=" + areaId +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}
