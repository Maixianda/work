package com.gzliangce.entity;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.gson.reflect.TypeToken;
import com.gzliangce.AppContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.Files;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.gson.Gsons;

/**
 * Created by aaron on 2/24/16.
 */
public class ChatConversation implements Comparable<ChatConversation> {

    private String icon;
    private String name;
    private String lastMessage;
    private long time;
    private String memberId;
    private String conversationId;
    private int unReadCount = 0;
    private Type type;
    private boolean checked = false;
    private boolean isStaff = false;
    private AVIMConversation conversation;
    private List<MembersInfo> membersInfos = new ArrayList<>();

    public ChatConversation(AVIMConversation conversation, boolean isStaff) {
        this.conversation = conversation;
        this.isStaff = isStaff;
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
                this.name = membersInfo.getNickname();
                if (isStaff) {
                    this.icon = Files.getAssetFile("ic_staff.png");
                    this.name = "在线客服";
                }
                this.memberId = membersInfo.getId();
                break;
            }
        }
        try {
            this.lastMessage = conversation.getAttribute("lastmessage").toString();
        } catch (Exception e) {
            this.lastMessage = "";
        }
        if (conversation.getLastMessageAt() != null) {
            this.time = conversation.getLastMessageAt().getTime();
        } else {
            this.time = conversation.getCreatedAt().getTime();
        }
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public List<MembersInfo> getMembersInfos() {
        return membersInfos;
    }

    public void setMembersInfos(List<MembersInfo> membersInfos) {
        this.membersInfos = membersInfos;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getConversationId() {
        return conversation.getConversationId();
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "ChatConversation{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", time=" + time +
                ", memberId='" + memberId + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", unReadCount=" + unReadCount +
                ", type=" + type +
                ", checked=" + checked +
                ", isStaff=" + isStaff +
                ", conversation=" + conversation +
                ", membersInfos=" + membersInfos +
                '}';
    }

    @Override
    public int compareTo(ChatConversation another) {
        return (int) (this.getTime() - another.getTime());
    }
}
