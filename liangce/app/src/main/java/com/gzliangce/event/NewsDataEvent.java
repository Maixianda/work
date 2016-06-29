package com.gzliangce.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/3/17.
 * 资讯数据更新Event
 */
public class NewsDataEvent implements Event {
    boolean isRefresh;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public NewsDataEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    public String toString() {
        return "NewsDataEvent{" +
                "isRefresh=" + isRefresh +
                '}';
    }
}
