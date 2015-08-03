package com.partner.common.util;

import android.text.TextUtils;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.constant.HttpConsts;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by emilyu on 6/27/15.
 */
public class HttpUtils {
    public static String getUserSign() {
        String udid = PartnerApplication.getInstance().getImei();
        long time = new Date().getTime()/1000;
        String sig = TextUtils.isEmpty(udid)?"":udid + "hyl" + time;
        return "&sig=" + Utils.getMD5Code(sig) + "&time=" + time + "&udid=" + udid;
    }

    public static HashMap getUserSignMap() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String udid = PartnerApplication.getInstance().getImei();
        long time = new Date().getTime()/1000;
        String sig = TextUtils.isEmpty(udid)?"":udid + "hyl" + time;

        paramsMap.put("sig", Utils.getMD5Code(sig));
        paramsMap.put("time", String.valueOf(time));
        paramsMap.put("udid", udid);

        return paramsMap;
    }

    public static boolean checkResponse(Response response) {
        boolean isSuccess = false;
        if (response.isSuccessful()) {
            isSuccess = true;
        } else {
            Toaster.showMsg("Unexpected code " + response);
        }

        return isSuccess;
    }

    public static String getResponseBody(Response response) {
        try {
            if(checkResponse(response)) {
                return response.body().string();
            }
        } catch (IOException e) {
            Toaster.showMsg("Unexpected code " + response);
        }

        return null;
    }

    public static String getResponseData(Response response) {
        return getResponseData(response, true);
    }

    public static String getResponseData(Response response, boolean isShowErrorMsg) {
        String body = getResponseBody(response);
        Logcat.d("response body : " + body);
        try {
            JSONObject bodyObject = new JSONObject(body);
            int errorCode = bodyObject.optInt("error_code");
            if(errorCode == HttpConsts.RESPONSE_CODE_SUCCESS) {
                return bodyObject.optString("data");
            } else {
                if(isShowErrorMsg) {
                    Toaster.showMsg(bodyObject.optString("error_message"));
                }
                return null;
            }
        } catch (JSONException e) {
            Toaster.show(R.string.data_get_error);
            return null;
        }
    }
}
