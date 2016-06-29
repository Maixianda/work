package com.gzliangce.event;

import com.gzliangce.entity.OrderInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/16/16.
 */
public class RefreshListEvent implements Event {
    private boolean refresh;
    private int position;
    private OrderInfo orderInfo;

    public RefreshListEvent(boolean refresh) {
        this.refresh = refresh;
    }

    public RefreshListEvent(boolean refresh, int position, OrderInfo orderInfo) {
        this.refresh = refresh;
        this.position = position;
        this.orderInfo = orderInfo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "RefreshListEvent{" +
                "refresh=" + refresh +
                ", position=" + position +
                '}';
    }
}
