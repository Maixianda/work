package com.gzliangce.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/25/16.
 */
public class ImMessagePushEvent implements Event {
    public AVIMMessage message;
    public AVIMConversation conversation;

    public ImMessagePushEvent(AVIMMessage message, AVIMConversation conversation) {
        this.message = message;
        this.conversation = conversation;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

}
