package com.partner.model;

import java.io.Serializable;

import javax.sql.StatementEvent;

/**
 * Created by yuym on 2015/7/4.
 */
public class FriendInfo implements Serializable{

    private int friendId;

    //备注名
    private String friendMyName;

    private String friendNickName;

    private String friendRealName;

    private String friendCellphone;

    private String friendHeadImage;

    //报名用户
    private String nickname;

    private String username;

    private String userinrollcellphone;

    private String headimage;

    private boolean isfriend;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendRealName() {
        return friendRealName;
    }

    public void setFriendRealName(String friendRealName) {
        this.friendRealName = friendRealName;
    }

    public String getFriendCellphone() {
        return friendCellphone;
    }

    public void setFriendCellphone(String friendCellphone) {
        this.friendCellphone = friendCellphone;
    }

    public String getFriendMyName() {
        return friendMyName;
    }

    public void setFriendMyName(String friendMyName) {
        this.friendMyName = friendMyName;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getFriendHeadImage() {
        return friendHeadImage;
    }

    public void setFriendHeadImage(String friendHeadImage) {
        this.friendHeadImage = friendHeadImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserinrollcellphone() {
        return userinrollcellphone;
    }

    public void setUserinrollcellphone(String userinrollcellphone) {
        this.userinrollcellphone = userinrollcellphone;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public boolean isfriend() {
        return isfriend;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }
}
