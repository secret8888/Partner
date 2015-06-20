package com.partner.common.http;

import com.partner.common.constant.HttpConsts;
import com.squareup.okhttp.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuym on 6/10/15.
 */
public class HttpManager {

    public static void login(String phone, String password,
                             String verifyCode, Callback callback) {
        Map<String, String> values = new HashMap<>();
        values.put("phone", phone);
        values.put("password", password);
        values.put("verifyCode", verifyCode);
        PartnerHttpClient.asyncPost(HttpConsts.BASE_URL, values, callback);

    }
}
