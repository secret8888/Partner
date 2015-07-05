package com.partner.model;

import java.io.Serializable;

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
}
