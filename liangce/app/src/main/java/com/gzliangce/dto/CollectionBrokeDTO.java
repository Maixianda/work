package com.gzliangce.dto;

import com.gzliangce.entity.BrokeInfo;

import java.util.List;

/**
 * Created by leo on 16/1/29.
 * 收藏经纪人
 */
public class CollectionBrokeDTO extends BaseDTO {
    private List<BrokeInfo> list;

    public List<BrokeInfo> getList() {
        return list;
    }

    public void setList(List<BrokeInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CollectionBrokeDTO{" +
                "list=" + list +
                '}';
    }
}
