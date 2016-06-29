package com.gzliangce.event;

import com.gzliangce.entity.PlaceAnOrder;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/4/1.
 * 转单Event
 */
public class ChangeOrderEvent implements Event {
    private PlaceAnOrder placeAnOrder;
    private Class mClass;

    public Class getmClass() {
        return mClass;
    }

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public ChangeOrderEvent(PlaceAnOrder placeAnOrder, Class mClass) {
        this.placeAnOrder = placeAnOrder;
        this.mClass = mClass;
    }

    public PlaceAnOrder getPlaceAnOrder() {
        return placeAnOrder;
    }

    public void setPlaceAnOrder(PlaceAnOrder placeAnOrder) {
        this.placeAnOrder = placeAnOrder;
    }

    @Override
    public String toString() {
        return "ChangeOrderEvent{" +
                "placeAnOrder=" + placeAnOrder +
                '}';
    }
}
