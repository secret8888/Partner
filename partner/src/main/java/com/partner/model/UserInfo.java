package com.partner.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author yuym
 */
public class UserInfo {

    private String token;

    @SerializedName("username")
    private String userName;

    private String cellphone;

    @SerializedName("nickname")
    private String nickName;

    @SerializedName("headimage")
    private String headImage;

    @SerializedName("usersex")
    private int userSex;

    @SerializedName("usertype")
    private int userType;

    private String address;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
