package com.partner.model;

import java.util.ArrayList;

/**
 * Created by yuym on 2015/7/4.
 */
public class FriendList {
    private ArrayList<FriendInfo> friends;

    private ArrayList<FriendInfo> orgs;

    public ArrayList<FriendInfo> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<FriendInfo> friends) {
        this.friends = friends;
    }

    public ArrayList<FriendInfo> getOrgs() {
        return orgs;
    }

    public void setOrgs(ArrayList<FriendInfo> orgs) {
        this.orgs = orgs;
    }
}
