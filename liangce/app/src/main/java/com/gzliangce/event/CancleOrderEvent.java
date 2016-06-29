package com.gzliangce.event;

import com.gzliangce.entity.OrderInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/4/1.
 * 订单被取消
 */
public class CancleOrderEvent implements Event {
    private OrderInfo orderInfo;
    private Class mClass;

    public Class getmClass() {
        return mClass;
    }

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public CancleOrderEvent(OrderInfo orderInfo, Class mClass) {
        this.orderInfo = orderInfo;
        this.mClass = mClass;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "CancleOrderEvent{" +
                "orderInfo=" + orderInfo +
                '}';
    }
}
