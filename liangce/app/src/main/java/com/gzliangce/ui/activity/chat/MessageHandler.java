package com.gzliangce.ui.activity.chat;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.event.ImMessagePushEvent;
import com.gzliangce.util.AVImClientManagerUtil;
import com.gzliangce.util.NotificationUtils;

import io.ganguo.library.core.event.EventHub;

/**
 */
public class MessageHandler extends AVIMMessageHandler {

    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        String clientID = "";
        try {
            clientID = AVImClientManagerUtil.getInstance().getClientId();
            if (client.getClientId().equals(clientID)) {
                // 过滤掉自己发的消息
                if (!message.getFrom().equals(clientID)) {
                    sendEvent(message, conversation);
//                    if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
//                        sendNotification(message, conversation);
//                    }
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }
    }

    /**
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     *
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMMessage message, AVIMConversation conversation) {
        EventHub.post(new ImMessagePushEvent(message, conversation));
    }

    private void sendNotification(AVIMMessage message, AVIMConversation conversation) {
        String notificationContent = message instanceof AVIMMessage ?
                message.getContent() : context.getString(R.string.unspport_message_type);
        Intent intent = new Intent(context, MessageBroadcastReceiver.class);
        intent.putExtra(Constants.CHAT_CONVERSATION_ID, conversation.getConversationId());
        intent.putExtra(Constants.CHAT_MEMBER_ID, message.getFrom());
        NotificationUtils.showNotification(context, "", notificationContent, null, intent);
    }
}
