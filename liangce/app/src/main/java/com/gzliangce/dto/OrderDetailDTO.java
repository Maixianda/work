package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.OrderInfo;

import java.io.Serializable;

/**
 * Created by aaron on 2/2/16.
 */
public class OrderDetailDTO extends BaseDTO implements Serializable {
    @SerializedName("orderDetail")
    private OrderInfo orderDetail;

    public OrderInfo getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderInfo orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "orderDetail=" + orderDetail +
                '}';
    }
}
