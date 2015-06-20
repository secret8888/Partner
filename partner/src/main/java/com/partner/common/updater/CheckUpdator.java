package com.partner.common.updater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.common.util.Logcat;
import com.partner.R;
import com.youdao.yjson.YJson;

public class CheckUpdator {

    private static CheckUpdator checkUpdator = null;

    private static final String TAG = CheckUpdator.class.getSimpleName();

    private CheckUpdator() {
    }

    public static CheckUpdator getInstance() {
        if (checkUpdator == null) {
            checkUpdator = new CheckUpdator();
        }

        return checkUpdator;
    }

//    public void checkUpdate(final Activity activity, final HttpResultFilter.OnHttpResultListener listener) {
//        VolleyManager.getInstance().doStringRequest(new BaseRequest() {
//            @Override
//            public String getURL() {
//                return String.format(HttpConsts.APP_UPDATE_URL, Env.agent().version());
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                return YDUserManager.getInstance(activity).getCookieHeader();
//            }
//        }, new VolleyManager.Listener<String>() {
//            @Override
//            public void onSuccess(String s) {
//                HttpResultFilter.checkHttpResult(activity, s, listener);
//            }
//
//            @Override
//            public void onError(VolleyError volleyError) {
//                Toaster.show(activity, R.string.network_connect_timeout);
//                listener.onHttpError(HttpResultFilter.HTTP_ERROR, volleyError.getMessage());
//            }
//        });
//    }

    public void parseUpdateResponse(Activity activity, String response) {
        Logcat.d(TAG, "result : " + response.toString());
        AppInfo appInfo = YJson.getObj(response, AppInfo.class);
        if (appInfo == null) {
            return;
        }
        if (appInfo.newVersion) {
            showUpdateDialog(activity, appInfo);
        }
    }

    private void showUpdateDialog(final Activity context, final AppInfo result) {
        Builder build = new Builder(context);
        build.setTitle(R.string.new_version_tip);
        build.setNegativeButton(R.string.update, new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ApkDownloadUtil.getInstance().initAppDownloadInfo(context.getResources().getString(R.string.app_name),
                        "com.partner", result.downloadUrl);
                ApkDownloadUtil.getInstance().startDownload(context, null,
                        PartnerApplication.class, true);
            }
        });
        //TODO
        //build.setPositiveButton(R.string.cancel, null);
        setAlertDialogContent(context, result, build);
        final AlertDialog alertDialog = build.create();
        Button positiveBtn = alertDialog
                .getButton(DialogInterface.BUTTON_NEGATIVE);
        if (positiveBtn != null) {
            positiveBtn.setTextColor(Color.RED);
        }
        alertDialog.show();
    }

    private void setAlertDialogContent(Activity activity, AppInfo result,
                                       Builder build) {
        View contentView = activity.getLayoutInflater().inflate(
                R.layout.dialog_content, null);
        TextView contentTxt = (TextView) contentView
                .findViewById(R.id.update_content);
        TextView tipsTxt = (TextView) contentView
                .findViewById(R.id.update_tips);
        build.setView(contentView);
        contentTxt.setText(activity.getResources().getString(R.string.update_content_tip) + result.updateTxt.get(0) + "\n");

        String tips = null;
        String totalSize = activity.getResources().getString(R.string.update_size_tip) + result.size + "\n";

        tips = totalSize;
        SpannableString sp = new SpannableString(tips);
        ForegroundColorSpan bgSpan = new ForegroundColorSpan(Color.RED);
        int start = tips.indexOf(result.size);
        sp.setSpan(bgSpan, start, tips.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipsTxt.setText(sp);
    }

}
