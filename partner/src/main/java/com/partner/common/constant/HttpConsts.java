package com.partner.common.constant;

import android.os.Environment;

import com.partner.BuildConfig;

/**
 * Created by yuym on 4/2/15.
 */
public class HttpConsts {

    //服务端地址
    public static final String BASE_URL = "http://101.200.235.17:8080/playfun";

    //服务端地址
    public static final String LOGIN_URL = BASE_URL + "/login?cellphone=%s&password=%s";

    //获取验证码
    public static final String GET_CODE_URL = BASE_URL + "/getVerifyCode?cellphone=%s";

    //注册地址
    public static final String REGISTER_URL = BASE_URL + "/register?cellphone=%s&verifycode=%s&type=%d&password=%s";

    //重置密码地址
    public static final String RESET_PSD_URL = BASE_URL + "/resetPassword?cellphone=%s&verifycode=%s&type=%d&password=%s";

    public static final int RESPONSE_CODE_SUCCESS = 0;

    //请求成功
    public static final int REQUEST_SUCCESS = 0;

    //请求失败
    public static final int REQUEST_FAIL = 1;
}
