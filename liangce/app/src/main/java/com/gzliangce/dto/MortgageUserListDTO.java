package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.BrokeInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/1/25.
 * 按揭员列表
 */
public class MortgageUserListDTO extends BaseDTO implements Serializable {
    private int page;

    @SerializedName("list")
    private List<BrokeInfo> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<BrokeInfo> getList() {
        return list;
    }

    public void setList(List<BrokeInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MortgageUserListDTO{" +
                "page=" + page +
                ", list=" + list +
                '}';
    }
}
