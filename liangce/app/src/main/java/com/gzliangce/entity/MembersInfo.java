package com.gzliangce.entity;

/**
 * Created by aaron on 2/27/16.
 */
public class MembersInfo {
    private String id;
    private String avatar;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "MembersInfo{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
