package com.partner.common.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.facebook.common.file.FileUtils;
import com.partner.R;
import com.partner.common.constant.Consts;
import com.youdao.sharesdk.constant.ShareConsts;
import com.youdao.sharesdk.listener.OnSharePlatformListener;
import com.youdao.sharesdk.platform.other.SystemShareClient;
import com.youdao.sharesdk.platform.qq.QQClient;
import com.youdao.sharesdk.platform.qq.QZoneClient;
import com.youdao.sharesdk.platform.weibo.WeiBoClient;
import com.youdao.sharesdk.platform.weixin.WeiXinClient;
import com.youdao.sharesdk.view.ShareAlert;

/**
 * share sdk manager. if the share image is from web, you must download it firstly.
 * The platform of qq can share web url directly, but system share only can share local uri,
 * and the left platforms can share image by the type of bitmap.
 * <p/>
 * Created by yuym on 12/1/14.
 */
public class ShareSDKManager {

    private Activity context = null;

    private String appName = null;

    private String defaultShareContent = null;

    private String website = null;

    private Dialog progressDialog = null;

    private static ShareSDKManager shareSDKManager = null;

    private static final int THUMB_SIZE = 200;

    private ShareSDKManager(Activity context) {
        this.context = context;
        appName = context.getResources().getString(R.string.app_name);
        defaultShareContent = context.getResources().getString(R.string.share_default_content);
        website = context.getResources().getString(R.string.share_website);
    }

    public static ShareSDKManager getInstance(Activity context) {
        if (shareSDKManager == null) {
            shareSDKManager = new ShareSDKManager(context);
        }
        shareSDKManager.setContext(context);
        return shareSDKManager;
    }

    private void setContext(Activity context) {
        this.context = context;
    }

    /**
     * share image
     *
     * @param imageUrl
     */
    public void shareImage(final String imageUrl) {
        ShareAlert.getInstance().showAlert(context, new OnSharePlatformListener() {
            @Override
            public void onSharePlatformClick(final int platform) {
                if (platform == ShareConsts.PLATFORM_QQ) {
                    QQClient.getInstance(context, Consts.QQ_APP_ID).shareToQQByDefault(appName, defaultShareContent,
                            website, imageUrl, appName);
                } else if (platform == ShareConsts.PLATFORM_QZONE) {
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    imageUrls.add(imageUrl);
                    QZoneClient.getInstance(context, Consts.QQ_APP_ID)
                            .shareToQZone(appName, defaultShareContent,
                                    website, imageUrls, null);
                } else {
                    new AsyncTask<Void, Void, Bitmap>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            showRefreshView(context);
                        }

                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            return Utils.getBitmapByUrl(imageUrl);
                        }

                        @Override
                        protected void onPostExecute(Bitmap shareBmp) {
                            super.onPostExecute(shareBmp);
                            hideRefreshView(context);
                            if (shareBmp != null) {
                                sharePlatformImage(platform, shareBmp);
                                shareBmp.recycle();
                            }
                        }

                    }.execute();
                }
            }
        });
    }

    /**
     * share web page
     *
     * @param title     share title
     * @param shareText share text
     * @param imageUrl  share image url
     * @param pageUrl   share web page url
     */
    public void shareWebPage(final String title, final String shareText, final String imageUrl, final String pageUrl) {
        ShareAlert.getInstance().showAlert(context, new OnSharePlatformListener() {
            @Override
            public void onSharePlatformClick(final int platform) {
                if (platform == ShareConsts.PLATFORM_QQ) {
                    QQClient.getInstance(context, Consts.QQ_APP_ID).shareToQQByDefault(title, shareText,
                            pageUrl, imageUrl, appName);
                } else if (platform == ShareConsts.PLATFORM_QZONE) {
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    imageUrls.add(imageUrl);
                    QZoneClient.getInstance(context, Consts.QQ_APP_ID)
                            .shareToQZone(title, shareText,
                                    pageUrl, imageUrls, null);
                } else {
                    new AsyncTask<Void, Void, Bitmap>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            showRefreshView(context);
                        }

                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            return Utils.getBitmapByUrl(imageUrl);
                        }

                        @Override
                        protected void onPostExecute(Bitmap shareBitmap) {
                            super.onPostExecute(shareBitmap);
                            hideRefreshView(context);
                            if (shareBitmap == null) {
                                shareBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                            }

                            sharePlatformWebPage(platform, title, shareText, pageUrl, shareBitmap);
                            shareBitmap.recycle();
                        }

                    }.execute();
                }
            }
        });
    }

    /**
     * share image by platform
     *
     * @param platform platform type
     * @param shareBmp share bitmap
     */
    private void sharePlatformImage(int platform, Bitmap shareBmp) {
        switch (platform) {
//            case ShareConsts.PLATFORM_YIXIN:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareImageByBitmap(false, shareBmp);
//                break;
//            case ShareConsts.PLATFORM_YIXIN_TIMELIME:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareImageByBitmap(true, shareBmp);
//                break;
            case ShareConsts.PLATFORM_WEIXIN:
                if (checkWXInstalled()) {
                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareImageByBitmap(false, shareBmp);
                }
                break;
            case ShareConsts.PLATFORM_WEIXIN_TIMELIME:
                if (checkWXInstalled()) {
                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareImageByBitmap(true, shareBmp);
                }
                break;
            case ShareConsts.PLATFORM_SINA:
                WeiBoClient.getInstance(context, Consts.WEIBO_APP_KEY).shareImageByBitmap(defaultShareContent, shareBmp);
                break;
            case ShareConsts.PLATFORM_SYSTEM:
                sharePublic(appName, defaultShareContent, shareBmp);
                break;

            default:
                break;
        }
    }

    /**
     * share web page by platform
     *
     * @param platform
     * @param title
     * @param shareText
     * @param pageUrl
     * @param shareBitmap
     */
    private void sharePlatformWebPage(int platform, String title, String shareText, String pageUrl, Bitmap shareBitmap) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(
                shareBitmap, THUMB_SIZE, THUMB_SIZE, true);
        switch (platform) {
//            case ShareConsts.PLATFORM_YIXIN:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareWebPage(title, shareText, false, pageUrl, thumbBmp);
//                break;
//            case ShareConsts.PLATFORM_YIXIN_TIMELIME:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareWebPage(title, shareText, true, pageUrl, thumbBmp);
//                break;
            case ShareConsts.PLATFORM_WEIXIN:
                if (checkWXInstalled()) {
                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareWebPage(title, shareText, false, pageUrl, thumbBmp);
                }
                break;
            case ShareConsts.PLATFORM_WEIXIN_TIMELIME:
                if (checkWXInstalled()) {
                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareWebPage(title, shareText, true, pageUrl, thumbBmp);
                }
                break;
            case ShareConsts.PLATFORM_SINA:
                WeiBoClient.getInstance(context, Consts.WEIBO_APP_KEY).shareWebPage(shareText, shareBitmap,
                        appName, defaultShareContent,
                        thumbBmp, pageUrl);
                break;
            case ShareConsts.PLATFORM_SYSTEM:
                sharePublic(title, shareText, shareBitmap);
                break;
            default:
                break;
        }
        thumbBmp.recycle();
    }

    /**
     * share by system
     *
     * @param title
     * @param shareBitmap
     */
    private void sharePublic(String title, String description, Bitmap shareBitmap) {
        Utils.savePngAtTemp(shareBitmap, Consts.PLATFORM_SHARE_NAME);
        SystemShareClient.getInstance().sharePublic(
                context, title, description,
                Uri.parse("file://" + Utils.toSDCardPath("Partner/partner//temp/share_temp.png")));
    }

    /**
     * check whether weixin is installed
     *
     * @return
     */
    private boolean checkWXInstalled() {
        if (!WeiXinClient.getInstance(context, Consts.WX_APP_ID)
                .isWXAppInstalled()) {
            Toaster.show(context, R.string.weixin_not_install);
            return false;
        }

        return true;
    }

    /**
     * show refresh view
     *
     * @param context
     */
    private void showRefreshView(Context context) {
        progressDialog = ViewUtils.createLoadingDialog(context, context.getResources().getString(R.string.wating_hint));
        progressDialog.show();
    }

    /**
     * hide refresh view
     *
     * @param context
     */
    private void hideRefreshView(Context context) {
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
