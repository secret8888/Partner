package com.youdao.sharesdk.platform.weixin;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youdao.sharesdk.R;
import com.youdao.sharesdk.listener.OnShareExecuteListener;
import com.youdao.sharesdk.util.ShareToaster;
import com.youdao.sharesdk.util.Util;

/**
 * a singleton pattern class which includes weixin share functions. by
 * "getInstance()", you can get client instance, then share the values that you
 * want by different methods. ".wxapi.WXEntryActivity.java" include callback of
 * weixin client. you can get message there.
 * 
 * weixin official
 *      documentation(https://open.weixin.qq.com/cgi-bin/frame?t=
 *      resource/res_main_tmpl&verify =1&lang=zh_CN)
 *      
 * @author yuym
 */
public class WeiXinClient {

	private Context context = null;

	private static WeiXinClient client = null;

	/*
	 * weixin api
	 */
	private IWXAPI api = null;

	/*
	 * the minimum timeline supported version
	 */
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	/*
	 * thumb size
	 */
	private static final int THUMB_SIZE = 200;

	private WeiXinClient(Context context, String appId) {
		this.context = context;
		api = WXAPIFactory.createWXAPI(context, appId, false);
	}

	public static WeiXinClient getInstance(Context context, String appId) {
		if (client == null) {
			client = new WeiXinClient(context, appId);
		}

		return client;
	}

	/**
	 * check whether weixin is installed
	 * 
	 * @return
	 */
	public boolean isWXAppInstalled() {
		return api.isWXAppInstalled();
	}

	/**
	 * check whether weixin support timeline
	 * 
	 * @return
	 */
	public boolean isTimelineSupported() {
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
			return true;
		}

		return false;
	}

	/**
	 * handler intent for callback from weixin app
	 * 
	 * @param intent
	 * @param handler
	 */
	public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
		api.handleIntent(intent, handler);
	}

	private void senReq(WXMediaMessage msg, boolean isShareTimeline, String type) {
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		// transaction is used to mark a request uniquely
		req.transaction = buildTransaction(type);
		req.message = msg;
		req.scene = isShareTimeline ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;

		// send msg to weixin
		api.sendReq(req);
	}

	private void shareImg(boolean isShareTimeline, WXImageObject imgObj,
			Bitmap shareBmp) {
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, THUMB_SIZE,
				THUMB_SIZE, true);
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		senReq(msg, isShareTimeline, "img");
	}

	/**
	 * send text msg to weixin
	 * 
	 * @param text
	 * @param isShareTimeline
	 *            whether is sent to timeline
	 */
	public void shareText(String text, boolean isShareTimeline) {
		if (TextUtils.isEmpty(text)) {
			ShareToaster.show(context, R.string.text_not_null);
			return;
		}

		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.description = text;

		senReq(msg, isShareTimeline, "text");
	}

	/**
	 * share image by bitmap
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param shareBmp
	 */
	public void shareImageByBitmap(boolean isShareTimeline, Bitmap shareBmp) {
		WXImageObject imgObj = new WXImageObject(shareBmp);
		shareImg(isShareTimeline, imgObj, shareBmp);
	}

	/**
	 * share image by local image path
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param localPath
	 */
	public void shareImageByPath(boolean isShareTimeline, String localPath) {
		File file = new File(localPath);
		if (!file.exists()) {
			ShareToaster.show(context, R.string.image_not_null);
			return;
		}

		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(localPath);

		Bitmap bmp = BitmapFactory.decodeFile(localPath);
		shareImg(isShareTimeline, imgObj, bmp);
		bmp.recycle();
	}

	/**
	 * share music by music url
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param musicUrl
	 * @param thumb
	 *            music thumb
	 */
	public void shareMusic(String title, String description,
			boolean isShareTimeline, String musicUrl, Bitmap thumb) {
		WXMusicObject music = new WXMusicObject();
		music.musicUrl = musicUrl;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = music;
		msg.title = title;
		msg.description = description;

		msg.thumbData = Util.bmpToByteArray(thumb, true);

		senReq(msg, isShareTimeline, "music");
	}

	/**
	 * share vedio by vedio url
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param vedioUrl
	 * @param thumb
	 */
	public void shareVideo(String title, String description,
			boolean isShareTimeline, String vedioUrl, Bitmap thumb) {
		WXVideoObject video = new WXVideoObject();
		video.videoUrl = vedioUrl;

		WXMediaMessage msg = new WXMediaMessage(video);
		msg.title = title;
		msg.description = description;
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		senReq(msg, isShareTimeline, "video");
	}

	/**
	 * share web page by url
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param pageUrl
	 * @param thumb
	 */
	public void shareWebPage(String title, String description,
			boolean isShareTimeline, String pageUrl, Bitmap thumb) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = pageUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		senReq(msg, isShareTimeline, "webpage");
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}
