package com.gzliangce.ui.activity.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.gzliangce.db.DBManager;

import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 用于操作未读消息数
 * <p>
 * Created by aaron on 29/2/2016.
 */
public class IMConversationHandler extends AVIMConversationEventHandler {
    private Logger logger = LoggerFactory.getLogger(IMConversationHandler.class);

    @Override
    public void onOfflineMessagesUnread(AVIMClient client, AVIMConversation conversation, int unreadCount) {
        DBManager.getInstance().add(conversation, unreadCount);
    }

    @Override
    public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {

    }

    @Override
    public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {

    }

    @Override
    public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }

    @Override
    public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }
}
