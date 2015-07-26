package com.youdao.sharesdk.platform.yixin;

import im.yixin.sdk.api.IYXAPI;
import im.yixin.sdk.api.SendMessageToYX;
import im.yixin.sdk.api.YXAPIFactory;
import im.yixin.sdk.api.YXImageMessageData;
import im.yixin.sdk.api.YXMessage;
import im.yixin.sdk.api.YXMusicMessageData;
import im.yixin.sdk.api.YXTextMessageData;
import im.yixin.sdk.api.YXVideoMessageData;
import im.yixin.sdk.api.YXWebPageMessageData;
import im.yixin.sdk.util.BitmapUtil;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.youdao.sharesdk.R;
import com.youdao.sharesdk.listener.OnShareExecuteListener;
import com.youdao.sharesdk.util.ShareToaster;
import com.youdao.sharesdk.util.Util;

/**
 * yixin share client, note: this client don't include callback function. if you
 * want to get the message from yixin, see:(http://open.yixin.im/start/android),
 * you must develop in ".yxapi.YXEntryActivity".
 * 
 * yixin offical documentation (http://open.yixin.im/start/android)
 * 
 * @author yuym
 */
public class YiXinClient {

	private Context context = null;

	private static YiXinClient client = null;

	private IYXAPI api;

	private YiXinClient(Context context, String appId) {
		this.context = context;
		api = YXAPIFactory.createYXAPI(context, appId);
		api.registerApp();
	}

	public static YiXinClient getInstance(Context context, String appId) {
		if (client == null) {
			client = new YiXinClient(context, appId);
		}

		return client;
	}

	/**
	 * check whether weixin is installed
	 * 
	 * @return
	 */
	public boolean isYXAppInstalled() {
		return api.isYXAppInstalled();
	}

	/**
	 * 
	 * @param msg
	 * @param isShareTimeline
	 * @param type
	 */
	private void senReq(YXMessage msg, boolean isShareTimeline, String type) {
		// create a Req
		SendMessageToYX.Req req = new SendMessageToYX.Req();
		// transaction tag a request uniquely
		req.transaction = buildTransaction(type);
		req.message = msg;
		req.scene = isShareTimeline ? SendMessageToYX.Req.YXSceneTimeline
				: SendMessageToYX.Req.YXSceneSession;

		// call api interface to send data to yixin
		api.sendRequest(req);
	}

	/**
	 * share image to yixin
	 * 
	 * @param isShareTimeline
	 *            is shared to timeline
	 * @param imgObj
	 * @param shareBmp
	 */
	private void shareImg(boolean isShareTimeline, YXImageMessageData imgObj,
			Bitmap shareBmp) {
		YXMessage msg = new YXMessage();
		msg.messageData = imgObj;
		Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, 200, 200, true);
		msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true); // 设置缩略图
		senReq(msg, isShareTimeline, "img");
	}

	/**
	 * send text msg to yixin
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

		YXTextMessageData textObj = new YXTextMessageData();
		textObj.text = text;

		YXMessage msg = new YXMessage();
		msg.messageData = textObj;
		// when send text message, title is ignored
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
		YXImageMessageData imgObj = new YXImageMessageData(shareBmp);
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
	public void shareImageByPath(final boolean isShareTimeline,
			final String localPath) {
		File file = new File(localPath);
		if (!file.exists()) {
			ShareToaster.show(context, R.string.image_not_null);
			return;
		}

		YXImageMessageData imgObj = new YXImageMessageData();
		imgObj.imagePath = localPath;

		Bitmap bmp = BitmapFactory.decodeFile(localPath);
		shareImg(isShareTimeline, imgObj, bmp);
		bmp.recycle();
	}

	/**
	 * share image by url
	 * 
	 * @param title
	 * @param description
	 * @param isShareTimeline
	 * @param url
	 */
	public void shareImageByUrl(final boolean isShareTimeline,
			final String url, final OnShareExecuteListener listener) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (listener != null) {
					listener.onPreShareExecute();
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					YXImageMessageData imgObj = new YXImageMessageData();
					imgObj.imageUrl = url;

					Bitmap bmp = Util.getBitmapByUrl(url);
					shareImg(isShareTimeline, imgObj, bmp);
					bmp.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (listener != null) {
					listener.onPostShareExecute();
				}
			}
		}.execute();
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
	 * @param listener
	 */
	public void shareMusic(String title, String description,
			boolean isShareTimeline, String musicUrl, Bitmap thumb) {
		YXMusicMessageData music = new YXMusicMessageData();
		music.musicUrl = musicUrl;

		YXMessage msg = new YXMessage();
		msg.messageData = music;
		msg.title = title;
		msg.description = description;

		msg.thumbData = BitmapUtil.bmpToByteArray(thumb, true);

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
		YXVideoMessageData video = new YXVideoMessageData();
		video.videoUrl = vedioUrl;

		YXMessage msg = new YXMessage(video);
		msg.title = title;
		msg.description = description;
		msg.thumbData = BitmapUtil.bmpToByteArray(thumb, true);

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
		YXWebPageMessageData webpage = new YXWebPageMessageData();
		webpage.webPageUrl = pageUrl;
		YXMessage msg = new YXMessage(webpage);
		msg.title = title;
		msg.description = description;
		msg.thumbData = BitmapUtil.bmpToByteArray(thumb, true);

		senReq(msg, isShareTimeline, "webpage");
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}
