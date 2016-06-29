package com.gzliangce.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import io.ganguo.library.core.event.Event;

/**
 * Created by aaron on 2/25/16.
 */
public class ImMessageSendEvent implements Event {
    public String message;
    public AVIMConversation conversation;

    public ImMessageSendEvent(String message, AVIMConversation conversation) {
        this.message = message;
        this.conversation = conversation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

}
