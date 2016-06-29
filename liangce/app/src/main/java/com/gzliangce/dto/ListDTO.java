package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 1/29/16.
 */
public class ListDTO<T> extends BaseDTO implements Serializable {

    @SerializedName("list")
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListDTO{" +
                "list=" + list +
                '}';
    }
}
