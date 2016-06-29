package com.gzliangce.event;

import com.gzliangce.entity.OrderInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/3/10.
 * 添加订单成功后Event
 */
public class AddOrderEvent implements Event {
    private OrderInfo orderInfo;

    public AddOrderEvent(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
