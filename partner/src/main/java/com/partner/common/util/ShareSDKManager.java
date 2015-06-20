package com.partner.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.constant.Consts;
import com.partner.listener.OnLoadingViewListener;

import java.util.ArrayList;

/**
 * share sdk manager. if the share image is from web, you must download it firstly.
 * The platform of qq can share web url directly, but system share only can share local uri,
 * and the left platforms can share image by the type of bitmap.
 * <p/>
 * Created by yuym on 12/1/14.
 */
public class ShareSDKManager implements OnLoadingViewListener {

    private Activity context = null;

    private String appName = null;

    private String defaultShareContent = null;

    private String website = null;

    private static final int THUMB_SIZE = 200;

    private Dialog loadingDialog = null;

    private static ShareSDKManager shareSDKManager = null;

    private ShareSDKManager(Activity context) {
        this.context = context;
        appName = context.getResources().getString(R.string.app_name);
        defaultShareContent = context.getResources().getString(R.string.share_default_content);
        website = context.getResources().getString(R.string.kouyu_website);
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

//    /**
//     * share image
//     *
//     * @param imageUrl
//     */
//    public void shareImage(final String imageUrl) {
//        ShareAlert.getInstance().showAlert(context, new OnSharePlatformListener() {
//            @Override
//            public void onSharePlatformClick(final int platform) {
//                if (platform == ShareConsts.PLATFORM_QQ) {
//                    QQClient.getInstance(context, Consts.QQ_APP_ID).shareToQQByDefault(appName, defaultShareContent,
//                            website, imageUrl, appName);
//                } else if (platform == ShareConsts.PLATFORM_QZONE) {
//                    ArrayList<String> imageUrls = new ArrayList<String>();
//                    imageUrls.add(imageUrl);
//                    QZoneClient.getInstance(context, Consts.QQ_APP_ID)
//                            .shareToQZone(appName, defaultShareContent,
//                                    website, imageUrls, null);
//                } else {
//                    new AsyncTask<Void, Void, Bitmap>() {
//
//                        @Override
//                        protected void onPreExecute() {
//                            super.onPreExecute();
//                            onShowLoadingDialog();
//                        }
//
//                        @Override
//                        protected Bitmap doInBackground(Void... params) {
//                            return PicUtils.getBitmapByUrl(imageUrl);
//                        }
//
//                        @Override
//                        protected void onPostExecute(Bitmap shareBmp) {
//                            super.onPostExecute(shareBmp);
//                            onDismissLoadingDialog();
//                            if (shareBmp != null) {
//                                sharePlatformImage(platform, shareBmp);
//                                shareBmp.recycle();
//                            }
//                        }
//
//                    }.execute();
//                }
//            }
//        });
//    }
//
//    /**
//     * share web page
//     *
//     * @param title     share title
//     * @param shareText share text
//     * @param imageUrl  share image url
//     * @param pageUrl   share web page url
//     */
//    public void shareWebPage(final String title, final String shareText, final String imageUrl, final String pageUrl) {
//        ShareAlert.getInstance().showAlert(context, new OnSharePlatformListener() {
//            @Override
//            public void onSharePlatformClick(final int platform) {
//                if (platform == ShareConsts.PLATFORM_QQ) {
//                    QQClient.getInstance(context, Consts.QQ_APP_ID).shareToQQByDefault(title, shareText,
//                            pageUrl, imageUrl, appName);
//                    DATracker.getInstance().trackEvent("ShareQQBtn");
//                } else if (platform == ShareConsts.PLATFORM_QZONE) {
//                    ArrayList<String> imageUrls = new ArrayList<String>();
//                    imageUrls.add(imageUrl);
//                    QZoneClient.getInstance(context, Consts.QQ_APP_ID)
//                            .shareToQZone(title, shareText,
//                                    pageUrl, imageUrls, null);
//                    DATracker.getInstance().trackEvent("ShareQzoneBtn");
//                } else {
//                    new AsyncTask<Void, Void, Bitmap>() {
//
//                        @Override
//                        protected void onPreExecute() {
//                            super.onPreExecute();
//                            onShowLoadingDialog();
//                        }
//
//                        @Override
//                        protected Bitmap doInBackground(Void... params) {
//                            return PicUtils.getBitmapByUrl(imageUrl);
//                        }
//
//                        @Override
//                        protected void onPostExecute(Bitmap shareBitmap) {
//                            super.onPostExecute(shareBitmap);
//                            onDismissLoadingDialog();
//                            if (shareBitmap == null) {
//                                shareBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//                            }
//                            sharePlatformWebPage(platform, title, shareText, pageUrl, shareBitmap);
//                            shareBitmap.recycle();
//                        }
//
//                    }.execute();
//                }
//            }
//        });
//    }
//
//    /**
//     * share image by platform
//     *
//     * @param platform platform type
//     * @param shareBmp share bitmap
//     */
//    private void sharePlatformImage(int platform, Bitmap shareBmp) {
//        switch (platform) {
//            case ShareConsts.PLATFORM_YIXIN:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareImageByBitmap(false, shareBmp);
//                break;
//            case ShareConsts.PLATFORM_YIXIN_TIMELIME:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareImageByBitmap(true, shareBmp);
//                break;
//            case ShareConsts.PLATFORM_WEIXIN:
//                if (checkWXInstalled()) {
//                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareImageByBitmap(false, shareBmp);
//                }
//                break;
//            case ShareConsts.PLATFORM_WEIXIN_TIMELIME:
//                if (checkWXInstalled()) {
//                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareImageByBitmap(true, shareBmp);
//                }
//                break;
//            case ShareConsts.PLATFORM_SINA:
//                WeiBoClient.getInstance(context, Consts.WEIBO_APP_KEY, Consts.WEIBO_REDIRECT_URL,
//                        Consts.WEIBO_SCOPE).shareImageByBitmap(defaultShareContent, shareBmp);
//                break;
//            case ShareConsts.PLATFORM_SYSTEM:
//                sharePublic(appName, defaultShareContent, shareBmp);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    /**
//     * share web page by platform
//     *
//     * @param platform
//     * @param title
//     * @param shareText
//     * @param pageUrl
//     * @param shareBitmap
//     */
//    private void sharePlatformWebPage(int platform, String title, String shareText, String pageUrl, Bitmap shareBitmap) {
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(
//                shareBitmap, THUMB_SIZE, THUMB_SIZE, true);
//        switch (platform) {
//            case ShareConsts.PLATFORM_YIXIN:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareWebPage(title, shareText, false, pageUrl, thumbBmp);
//                DATracker.getInstance().trackEvent("ShareYixinBtn");
//                break;
//            case ShareConsts.PLATFORM_YIXIN_TIMELIME:
//                YiXinClient.getInstance(context, Consts.YX_APP_ID).shareWebPage(title, shareText, true, pageUrl, thumbBmp);
//                DATracker.getInstance().trackEvent("ShareYixinCircleBtn");
//                break;
//            case ShareConsts.PLATFORM_WEIXIN:
//                if (checkWXInstalled()) {
//                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareWebPage(title, shareText, false, pageUrl, thumbBmp);
//                }
//                DATracker.getInstance().trackEvent("ShareWechatBtn");
//                break;
//            case ShareConsts.PLATFORM_WEIXIN_TIMELIME:
//                if (checkWXInstalled()) {
//                    WeiXinClient.getInstance(context, Consts.WX_APP_ID).shareWebPage(title, shareText, true, pageUrl, thumbBmp);
//                }
//                DATracker.getInstance().trackEvent("ShareWechatCircleBtn");
//                break;
//            case ShareConsts.PLATFORM_SINA:
//                WeiBoClient.getInstance(context, Consts.WEIBO_APP_KEY, Consts.WEIBO_REDIRECT_URL, Consts.WEIBO_SCOPE)
//                        .shareWebPage(shareText, shareBitmap, appName, defaultShareContent,
//                                thumbBmp, pageUrl);
//                DATracker.getInstance().trackEvent("ShareWeiboBtn");
//                break;
//            case ShareConsts.PLATFORM_SYSTEM:
//                sharePublic(title, shareText, shareBitmap);
//                DATracker.getInstance().trackEvent("SharePublicBtn");
//                break;
//            default:
//                break;
//        }
//        thumbBmp.recycle();
//    }
//
//    /**
//     * share by system
//     *
//     * @param title
//     * @param description
//     * @param shareBitmap
//     */
//    private void sharePublic(String title, String description, Bitmap shareBitmap) {
//        PicUtils.savePngAtTemp(shareBitmap, Consts.SHARE_CACHE_NAME);
//        SystemShareClient.getInstance().sharePublic(
//                context, title, description,
//                Uri.parse("file://" + Consts.TEMP_FILE_PATH + Consts.SHARE_CACHE_NAME));
//    }
//
//    /**
//     * check whether weixin is installed
//     *
//     * @return
//     */
//    private boolean checkWXInstalled() {
//        if (!WeiXinClient.getInstance(context, Consts.WX_APP_ID)
//                .isWXAppInstalled()) {
//            Toaster.show(context, R.string.weixin_not_install);
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void onShowLoadingDialog() {
        loadingDialog = ViewUtils.createLoadingDialog(context,
                PartnerApplication.getInstance().getString(R.string.wating_hint));
        loadingDialog.show();
    }

    @Override
    public void onShowLoadingDialog(String loadingText) {
    }

    @Override
    public void onDismissLoadingDialog() {
        if(loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
