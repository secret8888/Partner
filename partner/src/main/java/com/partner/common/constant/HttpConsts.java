package com.partner.common.constant;

import android.os.Environment;

import com.partner.BuildConfig;

/**
 * Created by yuym on 4/2/15.
 */
public class HttpConsts {

    public static final int RESPONSE_CODE_SUCCESS = 0;

    //请求成功
    public static final int REQUEST_SUCCESS = 0;

    //请求失败
    public static final int REQUEST_FAIL = 1;

    //服务端地址
    public static final String BASE_URL = "http://101.200.235.17:8080/playfun";

    //服务端地址
    public static final String LOGIN_URL = BASE_URL + "/login?cellphone=%s&password=%s";

    //获取验证码
    public static final String GET_CODE_URL = BASE_URL + "/getVerifyCode?cellphone=%s";

    //注册地址
    public static final String REGISTER_URL = BASE_URL + "/register?cellphone=%s&verifycode=%s&type=%d&password=%s&username=%s";

    //重置密码地址
    public static final String RESET_PSD_URL = BASE_URL + "/resetPassword?cellphone=%s&verifycode=%s&type=%d&password=%s";

    //获取用户信息地址
    public static final String GET_USER_INFO_URL = BASE_URL + "/getUserInfo?token=%s";

    //修改用户信息地址
    public static final String UPDATE_USER_INFO_URL = BASE_URL + "/updateUserInfo?token=%s";

    // 修改头像地址
    public static final String UPDATE_AVATAR_URL = BASE_URL + "/updateUserHeadimage";

    //获取所有报名人信息
    public static final String GET_ALL_REGISTRATION_URL = BASE_URL + "/getAllUserInrollInfos?token=%s";

    //添加报名人信息
    public static final String ADD_REGISTRATION_URL = BASE_URL + "/addUserInrollInfo?cellphone=%s&parentname=%s&token=%s";

    //修改报名人信息
    public static final String MODIFY_REGISTRATION_URL = BASE_URL + "/updateUserInrollInfo?cellphone=%s&parentname=%s&token=%s&userInrollInfoId=%d";

    //删除报名人
    public static final String DELETE_REGISTRATION_URL = BASE_URL + "/deleteUserInrollInfo?token=%s&userInrollInfoId=%d";

    //添加好友
    public static final String ADD_FRIEND_URL = BASE_URL + "/addFriend?token=%s&friendtoken=%s";

    //获取好友列表
    public static final String GET_FRIENDS_URL = BASE_URL + "/getFriends?token=%s&type=%d";

    //删除好友
    public static final String DELETE_FRIEND_URL = BASE_URL + "/deleteFriend?token=%s&friendId=%d";

    //修改朋友
    public static final String UPDATE_FRIEND_URL = BASE_URL + "/updateFriend?token=%s&friendId=%d&friendName=%s";

    //获取活动列表
    public static final String GET_ACTIVITIES_URL = BASE_URL + "/getActivities?start=%d&offset=%d&receivedIds=%s";

    //查看已经参加的活动
    public static final String GET_JOINED_ACTIVITIES_URL = BASE_URL + "/getJoinedActivities?token=%s&start=%d&offset=%d&receivedIds=%s";
}
