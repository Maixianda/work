package com.gzliangce.ui.activity.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.AVBroadcastReceiver;
import com.avos.avospush.notification.NotificationCompat;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.event.ImMessagePushEvent;
import com.gzliangce.event.ImRedPointEvent;
import com.gzliangce.event.MessageCenterRedPointEvent;
import com.gzliangce.ui.activity.usercenter.MessageCenterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;

/**
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 */
public class NotificationBroadcastReceiver extends AVBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent == null) {

                return;
            }
            if (intent.getExtras() == null) {

                return;
            }

            String action = intent.getAction();

            if (Strings.isEquals(action, "SystemMessage") || Strings.isEquals(action, "UserMessage")) {

                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                String message = json.getString("alert");

                Intent resultIntent = null;
                if (Strings.isEquals(action, "SystemMessage")) {
                    resultIntent = new Intent(context, MessageCenterActivity.class);
                } else if (Strings.isEquals(action, "UserMessage")) {
                    resultIntent = new Intent(context, ConversationActivity.class);
                    String memberId = json.getString("sender-id");
                    resultIntent.putExtra(Constants.CHAT_MEMBER_ID, memberId);
                    resultIntent.putExtra(Constants.IS_NEED_CLEAN_RED_POINT, true);
                }
                sendNotification(context, resultIntent, message, action);
            }

        } catch (JSONException e) {
            Log.e("TAG", "JSONException: " + e.getMessage());
        }
    }

    private void sendNotification(Context context, Intent resultIntent, String message, String action) {
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.m_app_name))
                        .setContentText(message)
                        .setTicker(message);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        int mNotificationId = (int) (1 + Math.random() * (10000 - 1 + 1));
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        if (Strings.isEquals(action, "SystemMessage")) {
            Config.putString(Constants.SHOW_WHITE_POINT, "enable");
            EventHub.post(new MessageCenterRedPointEvent());
        } else if (Strings.isEquals(action, "UserMessage")) {
            Config.putString(Constants.SHOW_RED_POINT, "enable");
            EventHub.post(new ImRedPointEvent());
        }

    }
}