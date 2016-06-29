package com.gzliangce.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/3/3.
 * 签约拍照 - event
 */
public class SignNewPhotoEvent implements Event {
    private int position;
    private boolean isUpLoad;

    public SignNewPhotoEvent(int position, boolean isUpLoad) {
        this.position = position;
        this.isUpLoad = isUpLoad;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isUpLoad() {
        return isUpLoad;
    }

    public void setUpLoad(boolean upLoad) {
        isUpLoad = upLoad;
    }

    @Override
    public String toString() {
        return "SignNewPhotoEvent{" +
                "position=" + position +
                ", isUpLoad=" + isUpLoad +
                '}';
    }
}
