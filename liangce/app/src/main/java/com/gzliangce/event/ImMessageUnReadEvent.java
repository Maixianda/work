package com.gzliangce.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/25/16.
 */
public class ImMessageUnReadEvent implements Event {
    public int unreadCount;
    public AVIMConversation conversation;

    public ImMessageUnReadEvent(int unreadCount, AVIMConversation conversation) {
        this.unreadCount = unreadCount;
        this.conversation = conversation;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

}
