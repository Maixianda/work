package com.gzliangce.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/2/17.
 * 详细月供 - 月份数据
 */
public class DetailedMonthyInfo implements Serializable {
    private List<MonthPaymentslyInfo> data;

    public List<MonthPaymentslyInfo> getData() {
        return data;
    }

    public void setData(List<MonthPaymentslyInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DetailedMonthyInfo{" +
                "data=" + data +
                '}';
    }
}
