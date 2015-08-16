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

    //获取粉丝
    public static final String GET_FANS_URL = BASE_URL + "/getFans?token=%s";

    //删除好友
    public static final String DELETE_FRIEND_URL = BASE_URL + "/deleteFriend?token=%s&friendId=%d";

    //修改朋友
    public static final String UPDATE_FRIEND_URL = BASE_URL + "/updateFriend?token=%s&friendId=%d&friendName=%s";

    //获取活动列表
    public static final String GET_ACTIVITIES_URL = BASE_URL + "/getActivities?start=%d&offset=%d&receivedIds=%s";

    //查看已经参加的活动
    public static final String GET_JOINED_ACTIVITIES_URL = BASE_URL + "/getJoinedActivities?token=%s&start=%d&offset=%d&receivedIds=%s";

    //查看已经参加的活动
    public static final String PUT_CHANNEL_ID_URL = BASE_URL + "/putChannelId?token=%s&channelId=%s&osType=3";

    //查看活动详情
    public static final String GET_ACTIVITY_DETAIL = BASE_URL + "/getActivityDetail?token=%s&activityId=%d";

    //获取活动报名人接口
    public static final String GET_SIGNED_USER = BASE_URL + "/getActivityJoinedUser?token=%s&activityId=%d";

    //活动参见
    public static final String SIGN_ACTIVITY = BASE_URL + "/joinActivity?token=%s&activityId=%d&childrenNum=%d&inrollIds=%s";

    //获取关注的机构
    public static final String FOLLOWED_INSTITUTION = BASE_URL + "/getOrgs?token=%s";

    //关注机构
    public static final String FOLLOW_ACTIVITY = BASE_URL + "/followActivity?token=%s&activityId=%d";

    //好友留言接口
    public static final String SEND_MESSAGE = BASE_URL + "/sendMessage?token=%s&userIds=%s&content=%s";

    //查看机构信息接口
    public static final String GET_INSTITUTION_INFO = BASE_URL + "/getOrgInfo?token=%s&id=%d";

    //关注好友
    public static final String FOLLOW_FRIEND = BASE_URL + "/followFriend?token=%s&friendtoken=%s";

    //邀请好友
    public static final String INVITE_ACTIVITY = BASE_URL + "/inviteActivity?token=%s&activityId=%d&inviteUserIds=%s";

    //获取已发布的活动
    public static final String GET_PUBLISHED_ACTIVITIES = BASE_URL + "/getActivitiesByOrg?token=%s&start=%d&offset=%d";

    //其他机构的所有活动
    public static final String OTHER_PUBLISHED_ACTIVITIES = BASE_URL + "/getActivitiesByOtherOrg?token=%s&userId=%d&receivedIds=%s&start=%d&offset=%d";

    //已经结束的活动，还想再举办，点击按钮，提交接口
    public static final String WANTED_FINISHED_ACTIVITY = BASE_URL + "/wantFinishedActivity?token=%s&activityId=%d";

    //获取消息列表
    public static final String GET_MESSAGES_URL = BASE_URL + "/getMessages?token=%s";

    //机构，某个活动下发出的所有通知的消息记录列表
    public static final String GET_MESSAGES_ACTIVITY_URL = BASE_URL + "/getMessagesByActivity?token=%s&activityId=%d";

    //机构，发活动
    public static final String PUBLISH_ACTIVITY_URL = BASE_URL + "/publishActivity";
}
