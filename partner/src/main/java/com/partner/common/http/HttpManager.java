package com.partner.common.http;

import android.text.TextUtils;
import android.util.Log;

import com.partner.PartnerApplication;
import com.partner.common.constant.HttpConsts;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Logcat;
import com.partner.common.util.Utils;
import com.squareup.okhttp.Callback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuym on 6/10/15.
 */
public class HttpManager {

    /**
     * 登录接口
     * @param phone
     * @param password
     * @param callback
     */
    public static void login(String phone, String password, AsyncHttpCallback callback) {
        String loginUrl = String.format(HttpConsts.LOGIN_URL, phone, Utils.getMD5Code(password));
        PartnerHttpClient.asyncGet(loginUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取验证码接口
     * @param phone
     * @param callback
     */
    public static void getCode(String phone, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.GET_CODE_URL, phone);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 注册接口
     * @param phone
     * @param password
     * @param code
     * @param type
     * @param callback
     */
    public static void register(String phone, String password, String code, int type,
                                String username, String address, String description, AsyncHttpCallback callback) {
        String registerUrl = String.format(HttpConsts.REGISTER_URL, phone, code, type, password, username);
        if(type == 1) {
            registerUrl += ("&address" + address + "&description" + description);
        }
        PartnerHttpClient.asyncGet(registerUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 重置密码接口
     * @param phone
     * @param password
     * @param code
     * @param type
     * @param callback
     */
    public static void resetPassword(String phone, String password, String code, int type, AsyncHttpCallback callback) {
        String registerUrl = String.format(HttpConsts.RESET_PSD_URL, phone, code, type, password);
        PartnerHttpClient.asyncGet(registerUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取用户信息接口
     * @param token
     * @param callback
     */
    public static void getUserInfo(String token, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.GET_USER_INFO_URL, token);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 修改用户信息接口
     * @param token
     * @param userName
     * @param nickName
     * @param address
     * @param description
     * @param callback
     */
    public static void updateUserInfo(String token, String userName, String nickName,
                                      String address, String description, String cellphone, AsyncHttpCallback callback) {
        String infoUrl = String.format(HttpConsts.UPDATE_USER_INFO_URL, token);
        if(!TextUtils.isEmpty(userName)) {
            infoUrl += "&username=" + userName;
        }
        if(!TextUtils.isEmpty(nickName)) {
            infoUrl += "&nickname=" + nickName;
        }
        if(!TextUtils.isEmpty(address)) {
            infoUrl += "&address=" + address;
        }
        if(!TextUtils.isEmpty(description)) {
            infoUrl += "&description=" + description;
        }
        if(!TextUtils.isEmpty(cellphone)) {
            infoUrl += "&cellphone=" + cellphone;
        }
        PartnerHttpClient.asyncGet(infoUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取用户信息接口
     * @param token
     * @param callback
     */
    public static void updateUserHeadImage(String token, File avatarFile, AsyncHttpCallback callback) {
        HashMap paramsMap = HttpUtils.getUserSignMap();
        paramsMap.put("token", token);
        PartnerHttpClient.asyncPostFile(HttpConsts.UPDATE_AVATAR_URL, paramsMap, avatarFile, callback);
    }

    /**
     * 获取所有报名人信息
     * @param token
     * @param callback
     */
    public static void getAllRegistList(String token, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.GET_ALL_REGISTRATION_URL, token);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 添加报名人
     * @param token
     * @param cellphone
     * @param parentName
     * @param callback
     */
    public static void addRegistrationInfo(String token, String cellphone, String parentName, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.ADD_REGISTRATION_URL, cellphone, parentName, token);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 修改报名人接口
     * @param token
     * @param userInrollInfoId
     * @param cellphone
     * @param parentName
     * @param callback
     */
    public static void modifyRegistrationInfo(String token, int userInrollInfoId, String cellphone, String parentName, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.MODIFY_REGISTRATION_URL, cellphone, parentName, token, userInrollInfoId);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 删除报名人
     * @param token
     * @param userInrollInfoId
     * @param callback
     */
    public static void deleteRegistrationInfo(String token, int userInrollInfoId, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.DELETE_REGISTRATION_URL, token, userInrollInfoId);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 添加好友
     * @param token
     * @param friendToken
     * @param callback
     */
    public static void addFriend(String token, String friendToken, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.ADD_FRIEND_URL, token, friendToken);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 查看好友
     * @param token
     * @param type
     * @param callback
     */
    public static void getFriendsList(String token, int type, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.GET_FRIENDS_URL, token, type);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 删除好友
     * @param token
     * @param friendId
     * @param callback
     */
    public static void deleteFriend(String token, int friendId, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.DELETE_FRIEND_URL, token, friendId);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 更新好友
     * @param token
     * @param friendId
     * @param friendName
     * @param callback
     */
    public static void updateFriend(String token, int friendId, String friendName, AsyncHttpCallback callback) {
        String codeUrl = String.format(HttpConsts.UPDATE_FRIEND_URL, token, friendId, friendName);
        PartnerHttpClient.asyncGet(codeUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取活动列表
     * @param callback
     */
    public static void getActivities(int start, int offset, String receivedIds, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_ACTIVITIES_URL, start, offset, receivedIds);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取已发布的活动
     * @param start
     * @param offset
     * @param callback
     */
    public static void getPublishedActivities(String token, int start, int offset, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_PUBLISHED_ACTIVITIES, token, start, offset);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 查看已经参加的活动
     * @param start
     * @param offset
     * @param receivedIds
     * @param callback
     */
    public static void getJoinedActivities(String token, int start, int offset, String receivedIds, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_JOINED_ACTIVITIES_URL, token, start, offset, receivedIds);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 推送接口，传递channelId参数
     * @param token
     * @param channelId
     */
    public static void putChannelId(String token, String channelId) {
        String url = String.format(HttpConsts.PUT_CHANNEL_ID_URL, token, channelId);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign());
    }

    /**
     * 获取活动详情
     * @param token
     * @param activityId
     * @param callback
     */
    public static void getActivityDetail(String token, int activityId, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_ACTIVITY_DETAIL, token, activityId);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取活动报名人
     * @param token
     * @param activityId
     * @param callback
     */
    public static void getSignedUsers(String token, int activityId, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_SIGNED_USER, token, activityId);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 参加活动
     * @param token
     * @param activityId
     * @param childrenNum
     * @param inrollIds
     * @param callback
     */
    public static void signActivity(String token, int activityId, int childrenNum, String inrollIds, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.SIGN_ACTIVITY, token, activityId, childrenNum, inrollIds);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 获取关注的机构
     * @param token
     * @param callback
     */
    public static void getFollowedInstitutions(String token, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.FOLLOWED_INSTITUTION, token);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 关注活动的举办方
     * @param token
     * @param callback
     */
    public static void followActivity(String token, int activityId, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.FOLLOW_ACTIVITY, token, activityId);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 好友留言接口
     * @param token
     * @param userIds
     * @param callback
     */
    public static void sendMessage(String token, String userIds, String content, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.SEND_MESSAGE, token, userIds, content);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 查看机构信息接口
     * @param token
     * @param id
     * @param callback
     */
    public static void getOrgInfo(String token, int id, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.GET_INSTITUTION_INFO, token, id);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 关注好友
     * @param token
     * @param friendToken
     * @param callback
     */
    public static void followFriend(String token, String friendToken, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.FOLLOW_FRIEND, token, friendToken);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }

    /**
     * 邀请好友
     * @param token
     * @param activityId
     * @param inviteUserIds
     * @param callback
     */
    public static void inviteFriend(String token, int activityId, String inviteUserIds, AsyncHttpCallback callback) {
        String url = String.format(HttpConsts.INVITE_ACTIVITY, token, activityId, inviteUserIds);
        PartnerHttpClient.asyncGet(url + HttpUtils.getUserSign(), callback);
    }
}
