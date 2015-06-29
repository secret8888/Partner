package com.partner.common.http;

import com.partner.common.constant.HttpConsts;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Utils;
import com.squareup.okhttp.Callback;

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
        String registerUrl = String.format(HttpConsts.REGISTER_URL, phone, code, type, password);
        if(type == 1) {
            registerUrl += ("&username=" + username + "&address" + address + "&description" + description);
        }
        PartnerHttpClient.asyncGet(registerUrl + HttpUtils.getUserSign(), callback);
    }

    /**
     * 充值密码接口
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
}
