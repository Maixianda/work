package com.gzliangce.event;

import com.gzliangce.entity.QualificationImageInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/1/15.
 * 预览照片界面  - 删除
 */
public class DeletePreviewImageEvent implements Event {
    private QualificationImageInfo info;

    public DeletePreviewImageEvent(QualificationImageInfo info) {
        this.info = info;
    }

    public QualificationImageInfo getInfo() {
        return info;
    }

    public void setInfo(QualificationImageInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "DeletePreviewImageEvent{" +
                "info=" + info +
                '}';
    }
}
