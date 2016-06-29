package com.gzliangce.db;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.gzliangce.AppContext;
import com.gzliangce.entity.MembersInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.Files;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.gson.Gsons;

/**
 * Created by aaron on 3/5/16.
 */
public class ConversationHistory {

    public int id;
    public String icon;
    public String name;
    public String lastMessage;
    public long time;
    private Type type;
    public String memberId;
    public String conversationId;
    public int unreadCount;
    public AVIMConversation avimConversation;
    private AVIMConversation conversation;
    private List<MembersInfo> membersInfos = new ArrayList<>();

    public ConversationHistory() {
    }

    public ConversationHistory(AVIMConversation avimConversation) {
        this.avimConversation = avimConversation;
        type = new TypeToken<List<MembersInfo>>() {
        }.getType();
        membersInfos.clear();

        Object object;
        try {
            object = Gsons.fromJson(conversation.getAttribute("membersInfo").toString(), type);
        } catch (Exception e) {
            object = conversation.getAttribute("membersInfo");
        }
        membersInfos.addAll((List<MembersInfo>) object);

        getMemberInfo();
    }

    /**
     * 遍历拿出头像,id
     */
    private void getMemberInfo() {

        for (MembersInfo membersInfo : membersInfos) {
            if (!Strings.isEquals(membersInfo.getId(), AppContext.me().getUser().getAccountId() + "")) {
                this.icon = membersInfo.getAvatar();
                this.memberId = membersInfo.getId();
                break;
            }
        }
        this.name = conversation.getName();

        try {
            this.lastMessage = conversation.getAttribute("lastmessage").toString();
        } catch (Exception e) {
            this.lastMessage = "";
        }
        if (conversation.getLastMessageAt() != null) {
            this.time = conversation.getLastMessageAt().getTime();
        }
    }

    public ConversationHistory(String icon, String name, String lastMessage, long time, String memberId, String conversationId, int unreadCount) {
        this.icon = icon;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.memberId = memberId;
        this.conversationId = conversationId;
        this.unreadCount = unreadCount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "ConversationHistory{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", memberId='" + memberId + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", unreadCount=" + unreadCount +
                ", avimConversation=" + avimConversation +
                ", conversation=" + conversation +
                ", membersInfos=" + membersInfos +
                '}';
    }
}
