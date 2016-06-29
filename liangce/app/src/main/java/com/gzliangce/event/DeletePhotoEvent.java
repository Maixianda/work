package com.gzliangce.event;

import com.gzliangce.entity.QualificationImageInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/16/16.
 */
public class DeletePhotoEvent implements Event {
    private int position;

    public DeletePhotoEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "DeletePhotoEvent{" +
                "position=" + position +
                '}';
    }
}
