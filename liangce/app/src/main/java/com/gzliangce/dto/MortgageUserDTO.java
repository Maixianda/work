package com.gzliangce.dto;

import com.gzliangce.entity.MortgageInfo;

/**
 * Created by leo on 16/1/25.
 * 按揭员详情
 */
public class MortgageUserDTO extends BaseDTO {
    private MortgageInfo info;

    public MortgageInfo getInfo() {
        return info;
    }

    public void setInfo(MortgageInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "MortgageUserDTO{" +
                "info=" + info +
                '}';
    }
}
