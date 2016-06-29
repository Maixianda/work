package com.gzliangce.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/16/16.
 */
public class RefreshOrderListEvent implements Event {
    private boolean refresh;
    private int position;
    private String orderNumber;
    private Class mClass;

    public Class getmClass() {
        return mClass;
    }

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public RefreshOrderListEvent(String orderNumber, Class mClass) {
        this.orderNumber = orderNumber;
        this.mClass = mClass;
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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
